package tgb.btc.library.service.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.PaymentReceipt;
import tgb.btc.library.bean.bot.PaymentType;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.DealRepository;
import tgb.btc.library.repository.bot.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DealService extends BasePersistService<Deal> {

    private final DealRepository dealRepository;

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public DealService(BaseRepository<Deal> baseRepository, DealRepository dealRepository) {
        super(baseRepository);
        this.dealRepository = dealRepository;
    }

    public CryptoCurrency getCryptoCurrencyByPid(@Param("pid") Long pid) {
        return dealRepository.getCryptoCurrencyByPid(pid);
    }

    public boolean existByPid(Long pid) {
        return dealRepository.existsById(pid);
    }

    public Long getDealsCountByUserChatId(Long chatId) {
        return dealRepository.getPassedDealsCountByUserChatId(chatId);
    }

    public Deal getByPid(Long pid) {
        return dealRepository.findByPid(pid);
    }

    public void updatePaymentTypeByPid(PaymentType paymentType, Long pid) {
        dealRepository.updatePaymentTypeByPid(paymentType, pid);
    }

    public Long getPidActiveDealByChatId(Long chatId) {
        return dealRepository.getPidActiveDealByChatId(chatId);
    }

    public Long getCountPassedByUserChatId(Long chatId) {
        return dealRepository.getCountPassedByUserChatId(chatId);
    }

    public List<Long> getActiveDealPids() {
        return dealRepository.getActiveDealPids();
    }

    public Long getUserChatIdByDealPid(Long pid) {
        return dealRepository.getUserChatIdByDealPid(pid);
    }

    public List<Deal> getByDateBetween(LocalDate startDate, LocalDate endDate) {
        return dealRepository.getByDateBetween(startDate, endDate);
    }

    public List<Deal> getByDate(LocalDate dateTime) {
        return dealRepository.getPassedByDate(dateTime);
    }

    public DealType getDealTypeByPid(Long pid) {
        return dealRepository.getDealTypeByPid(pid);
    }

    @Transactional(readOnly = true)
    public List<PaymentReceipt> getPaymentReceipts(Long dealPid) {
        Deal deal = getByPid(dealPid);
        return new ArrayList<>(deal.getPaymentReceipts());
    }

    public Deal createNewDeal(DealType dealType, Long chatId) {
        Deal deal = new Deal();
        deal.setActive(false);
        deal.setPassed(false);
        deal.setDateTime(LocalDateTime.now());
        deal.setDate(LocalDate.now());
        deal.setDealType(dealType);
        deal.setUser(userRepository.findByChatId(chatId));
        Deal savedDeal = save(deal);
        userRepository.updateCurrentDealByChatId(savedDeal.getPid(), chatId);
        return savedDeal;
    }

    public boolean isFirstDeal(Long chatId) {
        return getDealsCountByUserChatId(chatId) < 1;
    }
}
