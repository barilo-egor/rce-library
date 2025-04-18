package tgb.btc.library.service.web.merchant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.MerchantConfig;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.interfaces.service.bean.bot.IMerchantConfigService;
import tgb.btc.library.service.properties.VariablePropertiesReader;
import tgb.btc.library.service.web.merchant.error.MerchantErrorLoggingService;
import tgb.btc.library.vo.RequisiteVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MerchantRequisiteService {

    private final Map<Merchant, IMerchantRequisiteService> merchantIMerchantRequisiteServiceMap;

    private final VariablePropertiesReader variablePropertiesReader;

    private final MerchantErrorLoggingService merchantErrorLoggingService;

    private final IMerchantConfigService merchantConfigService;

    public MerchantRequisiteService(VariablePropertiesReader variablePropertiesReader,
                                    List<IMerchantRequisiteService> merchantRequisiteServices,
                                    MerchantErrorLoggingService merchantErrorLoggingService,
                                    IMerchantConfigService merchantConfigService) {
        this.variablePropertiesReader = variablePropertiesReader;
        this.merchantErrorLoggingService = merchantErrorLoggingService;
        this.merchantConfigService = merchantConfigService;
        this.merchantIMerchantRequisiteServiceMap = new HashMap<>();
        for (IMerchantRequisiteService merchantService : merchantRequisiteServices) {
            this.merchantIMerchantRequisiteServiceMap.put(merchantService.getMerchant(), merchantService);
        }
    }

    public RequisiteVO getRequisites(Deal deal) {
        log.debug("Получение реквизитов для сделки №{}. Сумма={}, тип оплаты={}.", deal.getPid(), deal.getAmount(), deal.getPaymentType().getName());
        RequisiteVO requisiteVO = null;
        List<MerchantConfig> merchantConfigList = merchantConfigService.findAllByIsOnOrderByMerchantOrder(true).stream()
                .filter(config -> deal.getAmount().intValue() <= config.getMaxAmount())
                .toList();
        int maxAttemptCount = 5;
        for (MerchantConfig merchantConfig : merchantConfigList) {
            if (merchantConfig.getAttemptsCount() > maxAttemptCount) {
                maxAttemptCount = merchantConfig.getAttemptsCount();
            }
        }
        List<Merchant> merchantList = merchantConfigList.stream().map(MerchantConfig::getMerchant).toList();
        log.debug("Список мерчантов для сделки №{}: {}", deal.getPid(), merchantList.stream().map(Merchant::getDisplayName).collect(Collectors.joining(", ")));
        for (int i = 0; i < maxAttemptCount; i++) {
            for (MerchantConfig merchantConfig : merchantConfigList) {
                Merchant merchant = merchantConfig.getMerchant();
                log.debug("Количество попыток мерчанта {} {}, секунд задержки {} для сделки №{}.", merchant.getDisplayName(),
                        merchantConfig.getAttemptsCount(), merchantConfig.getDelay(), deal.getPid());
                try {
                    int merchantAttemptsCount = merchantConfig.getAttemptsCount();
                    if (merchantAttemptsCount < i + 1) {
                        continue;
                    }
                    log.debug("Попытка №{} мерчанта {} для сделки №{}.", i + 1, merchant.getDisplayName(), deal.getPid());
                    requisiteVO = merchantIMerchantRequisiteServiceMap.get(merchant).getRequisite(deal);
                    if (Objects.nonNull(requisiteVO)) {
                        log.debug("Реквизиты для сделки №{} получены. Мерчант={}, реквизиты={}.",
                                deal.getPid(), merchant.getDisplayName(), requisiteVO.getRequisite());
                        break;
                    } else {
                        log.debug("Реквизиты для сделки №{} мерчанта {} с попытки №{} не получены.", deal.getPid(), merchant.getDisplayName(), i + 1);
                    }
                } catch (Exception e) {
                    log.debug("Ошибка получения реквизитов мерчанта {} для сделки №{} c попытки №{}.", merchant.getDisplayName(), deal.getPid(), i + 1);
                    merchantErrorLoggingService.log(deal.getPid(), e);
                }
                try {
                    if (i < merchantConfig.getAttemptsCount() - 1) {
                        Thread.sleep(merchantConfig.getDelay() * 1000L);
                    }
                } catch (InterruptedException ignored) {
                }
            }
            if (Objects.nonNull(requisiteVO)) break;
        }
        if (Objects.isNull(requisiteVO)) {
            log.debug("Реквизиты для сделки {} у мерчантов получены не были.", deal.getPid());
        }
        return requisiteVO;
    }
}
