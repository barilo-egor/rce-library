package tgb.btc.library.service.process;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tgb.btc.api.web.INotifier;
import tgb.btc.library.bean.bot.AutoConfirmConfig;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.MerchantConfig;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.web.merchant.AutoConfirmType;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.process.IAutoConfirmDealService;
import tgb.btc.library.interfaces.service.bean.bot.IMerchantConfigService;
import tgb.btc.library.interfaces.service.bean.bot.deal.IModifyDealService;
import tgb.btc.library.interfaces.util.IBigDecimalService;
import tgb.btc.library.interfaces.web.ICryptoWithdrawalService;
import tgb.btc.library.vo.web.PoolDeal;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Slf4j
public class AutoConfirmDealService implements IAutoConfirmDealService {

    private final IMerchantConfigService merchantConfigService;

    private final ICryptoWithdrawalService cryptoWithdrawalService;

    private final IModifyDealService modifyDealService;

    private final INotifier notifier;

    private final String botUsername;

    private final IBigDecimalService bigDecimalService;

    public AutoConfirmDealService(IMerchantConfigService merchantConfigService,
                                  ICryptoWithdrawalService cryptoWithdrawalService,
                                  IModifyDealService modifyDealService, INotifier notifier,
                                  @Value("${bot.username}") String botUsername, IBigDecimalService bigDecimalService) {
        this.merchantConfigService = merchantConfigService;
        this.cryptoWithdrawalService = cryptoWithdrawalService;
        this.modifyDealService = modifyDealService;
        this.notifier = notifier;
        this.botUsername = botUsername;
        this.bigDecimalService = bigDecimalService;
    }

    public boolean match(Deal deal) {
        if (!DealType.isBuy(deal.getDealType())
                || !CryptoCurrency.CURRENCIES_WITH_AUTO_WITHDRAWAL.contains(deal.getCryptoCurrency())
                || Objects.isNull(deal.getMerchant())) {
            return false;
        }
        Merchant merchant = deal.getMerchant();
        MerchantConfig merchantConfig = merchantConfigService.getMerchantConfig(merchant);
        return merchantConfig.getAutoConfirmConfig(deal.getCryptoCurrency(), deal.getDeliveryType()).isPresent();
    }

    public synchronized void autoConfirmDeal(Deal deal) {
        if (DealStatus.CONFIRMED.equals(deal.getDealStatus())) {
            throw new BaseException("Заявка уже подтверждена.");
        }
        log.debug("Автоматический вывод сделки {}, мерчант {}", deal.getPid(), deal.getMerchant().name());
        MerchantConfig merchantConfig = merchantConfigService.getMerchantConfig(deal.getMerchant());
        AutoConfirmConfig autoConfirmConfig = merchantConfig.getAutoConfirmConfig(deal.getCryptoCurrency(),
                deal.getDeliveryType()).orElseThrow(() -> new BaseException("Для автоподтверждения требуется наличие конфига."));
        AutoConfirmType autoConfirmType = autoConfirmConfig.getAutoConfirmType();
        if (AutoConfirmType.AUTO_WITHDRAWAL.equals(autoConfirmType)) {
            log.debug("Сделка {} будет автоматически выведена.", deal.getPid());
            String hash = cryptoWithdrawalService.withdrawal(deal.getCryptoCurrency(), deal.getCryptoAmount(), deal.getWallet());
            modifyDealService.confirm(deal.getPid(), hash);
            new Thread(() -> cryptoWithdrawalService.deleteFromPool(botUsername, deal.getPid())).start();
            notifier.sendAutoWithdrawDeal("бота", "система", deal.getPid());
            notifier.notifyAutoConfirmDeal("Автоматически подтверждена сделка №<b>" + deal.getPid() + "</b> мерчанта <b>"
                    + deal.getMerchant().getDisplayName() + "</b>. Тип автоподтверждения: <b>"
                    + autoConfirmConfig.getAutoConfirmType().getDescription() + "</b>.", deal.getPid());
            log.debug("Сделка {} автоматически выведена и отправлена в группу запросов.", deal.getPid());
        } else {
            log.debug("Сделка {} будет автоматически добавлена в пул.", deal.getPid());
            cryptoWithdrawalService.addPoolDeal(PoolDeal.builder()
                    .pid(deal.getPid())
                    .address(deal.getWallet())
                    .bot(botUsername)
                    .amount(bigDecimalService.roundToPlainString(deal.getCryptoAmount(), deal.getCryptoCurrency().getScale()))
                    .addDate(LocalDateTime.now())
                    .deliveryType(deal.getDeliveryType())
                    .build());
            modifyDealService.updateDealStatusByPid(DealStatus.AWAITING_WITHDRAWAL, deal.getPid());
            log.debug("Сделка {} добавлена в пул.", deal.getPid());
        }
    }
}
