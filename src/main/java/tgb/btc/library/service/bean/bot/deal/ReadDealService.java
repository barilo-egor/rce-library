package tgb.btc.library.service.bean.bot.deal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.PaymentReceipt;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.web.merchant.dashpay.DashPayOrderStatus;
import tgb.btc.library.constants.enums.web.merchant.evopay.EvoPayStatus;
import tgb.btc.library.constants.enums.web.merchant.nicepay.NicePayStatus;
import tgb.btc.library.constants.enums.web.merchant.onlypays.OnlyPaysStatus;
import tgb.btc.library.constants.enums.web.merchant.paypoints.PayPointsStatus;
import tgb.btc.library.constants.enums.web.merchant.payscrow.OrderStatus;
import tgb.btc.library.interfaces.service.bean.bot.deal.IReadDealService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.deal.ReadDealRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReadDealService extends BasePersistService<Deal> implements IReadDealService {

    private ReadDealRepository readDealRepository;

    @Autowired
    public void setReadDealRepository(ReadDealRepository readDealRepository) {
        this.readDealRepository = readDealRepository;
    }

    @Override
    public Deal findByPid(Long pid) {
        return readDealRepository.findByPid(pid);
    }

    @Override
    public List<Deal> getDealsByPids(List<Long> pids) {
        return readDealRepository.getDealsByPids(pids);
    }

    @Override
    public List<Long> getPidsByChatIdAndStatus(Long chatId, DealStatus dealStatus) {
        return readDealRepository.getPidsByChatIdAndStatus(chatId, dealStatus);
    }

    @Override
    public List<Long> getPaidDealsPids() {
        return readDealRepository.getPaidDealsPids();
    }

    @Override
    public boolean dealsByUserChatIdIsExist(Long chatId, DealStatus dealStatus, Long countDeals) {
        return readDealRepository.dealsByUserChatIdIsExist(chatId, dealStatus, countDeals);
    }

    @Override
    public String getWalletFromLastPassedByChatIdAndDealTypeAndCryptoCurrency(Long chatId, DealType dealType,
                                                                              CryptoCurrency cryptoCurrency) {
        return readDealRepository.getWalletFromLastPassedByChatIdAndDealTypeAndCryptoCurrency(chatId, dealType, cryptoCurrency);
    }

    @Override
    public List<PaymentReceipt> getPaymentReceipts(Long dealPid) {
        Deal deal = findByPid(dealPid);
        return new ArrayList<>(deal.getPaymentReceipts());
    }

    @Override
    public List<Deal> getAllByDealStatusAndCryptoCurrency(DealStatus dealStatus, CryptoCurrency cryptoCurrency) {
        return readDealRepository.getAllByDealStatusAndCryptoCurrency(dealStatus, cryptoCurrency);
    }

    @Override
    public List<Deal> getAllNotFinalPayscrowStatuses() {
        return readDealRepository.getAllNotNewByMerchantAndOrderStatusesAfterDateTime(
                Merchant.PAYSCROW,
                OrderStatus.getNotFinal().stream().map(Enum::name).toList(),
                LocalDateTime.now().minusMinutes(30)
        );
    }

    @Override
    public List<Deal> getAllNotFinalDashPayStatuses() {
        return readDealRepository.getAllNotNewByMerchantAndOrderStatusesAfterDateTime(
                Merchant.DASH_PAY,
                DashPayOrderStatus.FINAL_STATUSES.stream().map(Enum::name).toList(),
                LocalDateTime.now().minusMinutes(30)
        );
    }

    @Override
    public List<Deal> getAllNotFinalPayPointsStatuses() {
        return readDealRepository.getAllNotNewByMerchantAndOrderStatusesAfterDateTime(
                Merchant.PAY_POINTS,
                PayPointsStatus.FINAL_STATUSES.stream().map(Enum::name).toList(),
                LocalDateTime.now().minusMinutes(30)
        );
    }

    @Override
    public List<Deal> getAllNotFinalOnlyPaysStatuses() {
        return readDealRepository.getAllNotNewByMerchantAndOrderStatusesAfterDateTime(
                Merchant.ONLY_PAYS,
                List.of(OnlyPaysStatus.WAITING.name()),
                LocalDateTime.now().minusMinutes(30)
        );
    }

    @Override
    public List<Deal> getAllNotFinalEvoPayStatuses() {
        return readDealRepository.getAllNotNewByMerchantAndOrderStatusesAfterDateTime(
                Merchant.EVO_PAY,
                EvoPayStatus.NOT_FINAL_STATUSES.stream().map(Enum::name).toList(),
                LocalDateTime.now().minusMinutes(30)
        );
    }

    @Override
    public List<Deal> getAllNotFinalNicePayStatuses() {
        return readDealRepository.getAllNotNewByMerchantAndOrderStatusesAfterDateTime(
                Merchant.NICE_PAY,
                NicePayStatus.NOT_FINAL_STATUSES.stream().map(Enum::name).toList(),
                LocalDateTime.now().minusMinutes(30)
        );
    }

    @Override
    public Deal getByAlfaTeamInvoiceId(String alfaTeamInvoiceId) {
        return readDealRepository.getByMerchantOrderId(alfaTeamInvoiceId);
    }

    @Override
    protected BaseRepository<Deal> getBaseRepository() {
        return readDealRepository;
    }

}
