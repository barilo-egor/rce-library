package tgb.btc.library.repository.bot;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.PaymentType;
import tgb.btc.library.constants.enums.bot.*;
import tgb.btc.library.repository.BaseRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public interface DealRepository extends BaseRepository<Deal> {

    /**
     * UPDATE
     */

    @Modifying
    @Query("update Deal set cryptoCurrency=:cryptoCurrency where pid=:pid")
    void updateCryptoCurrencyByPid(Long pid, CryptoCurrency cryptoCurrency);

    @Modifying
    @Query("update Deal set cryptoAmount=:cryptoAmount where pid=:pid")
    void updateCryptoAmountByPid(BigDecimal cryptoAmount, Long pid);

    @Modifying
    @Query("update Deal set amount=:amount where pid=:pid")
    void updateAmountByPid(BigDecimal amount, Long pid);

    @Modifying
    @Query("update Deal set discount=:discount where pid=:pid")
    void updateDiscountByPid(BigDecimal discount, Long pid);

    @Modifying
    @Query("update Deal set commission=:commission where pid=:pid")
    void updateCommissionByPid(BigDecimal commission, Long pid);

    @Modifying
    @Query("update Deal set isUsedReferralDiscount=:isUsedReferralDiscount where pid=:pid")
    void updateUsedReferralDiscountByPid(Boolean isUsedReferralDiscount, Long pid);

    @Modifying
    @Query("update Deal set wallet=:wallet where pid=:pid")
    void updateWalletByPid(String wallet, Long pid);

    @Modifying
    @Query("update Deal set paymentType=:paymentType where pid=:pid")
    void updatePaymentTypeByPid(PaymentType paymentType, Long pid);

    @Modifying
    @Query("update Deal set isUsedPromo=:isUsedPromo where pid=:pid")
    void updateIsUsedPromoByPid(Boolean isUsedPromo, Long pid);

    @Modifying
    @Query("update Deal set isActive=:isActive where pid=:pid")
    void updateIsActiveByPid(Boolean isActive, Long pid);

    @Modifying
    @Query("update Deal set dealStatus=:dealStatus where pid=:pid")
    void updateDealStatusByPid(DealStatus dealStatus, Long pid);

    @Modifying
    @Query(value = "update Deal set fiatCurrency=:fiatCurrency where pid=:pid")
    void updateFiatCurrencyByPid(Long pid, FiatCurrency fiatCurrency);

    @Modifying
    @Query(value = "update Deal set isPersonalApplied=:isPersonalApplied where pid=:pid")
    void updateIsPersonalAppliedByPid(Long pid, Boolean isPersonalApplied);

    @Modifying
    @Query(value = "update Deal set paymentType=null where paymentType.pid=:paymentTypePid")
    void updatePaymentTypeToNullByPaymentTypePid(Long paymentTypePid);

    @Modifying
    @Query(value = "update Deal set additionalVerificationImageId=:additionalVerificationImageId where pid=:pid")
    void updateAdditionalVerificationImageIdByPid(Long pid, String additionalVerificationImageId);

    void findAllByDealStatusNot(DealStatus dealStatus);

    @Modifying
    @Query("update Deal set deliveryType=:deliveryType where pid=:pid")
    void updateDeliveryTypeByPid(Long pid, DeliveryType deliveryType);

    /**
     * DELETE
     */

    @Modifying
    void deleteByUser_ChatId(Long chatId);

    @Modifying
    void deleteByPidIn(List<Long> pidList);

    /**
     * SELECT
     */

    @Query("select cryptoCurrency from Deal where pid=:pid")
    CryptoCurrency getCryptoCurrencyByPid(Long pid);

    @Query("select commission from Deal where pid=:pid")
    BigDecimal getCommissionByPid(Long pid);

    @Query("select count(d) from Deal d where d.user.chatId=:chatId and d.dealStatus not in :dealStatus")
    Integer getCountFinishedDeal(Long chatId, List<DealStatus> dealStatus);

    @Query("select d.pid from Deal d where d.user.chatId=:chatId and d.dealStatus=:dealStatus")
    List<Long> getListNewDeal(Long chatId, DealStatus dealStatus);

    @Query("select count(d) from Deal d where d.user.chatId=:chatId and d.isPassed=true and d.isActive=false")
    Long getPassedDealsCountByUserChatId(Long chatId);

    @Query("select count(d) from Deal d where d.user.chatId=:chatId and d.isPassed=true and d.dealType=:dealType")
    Long getPassedDealsCountByUserChatId(Long chatId, DealType dealType);

    @Query("select count(d) from Deal d where d.user.chatId=:chatId and d.isPassed=true and d.dealType=:dealType and d.cryptoCurrency=:cryptoCurrency")
    Long getPassedDealsCountByUserChatIdAndDealTypeAndCryptoCurrency(Long chatId, DealType dealType, CryptoCurrency cryptoCurrency);

    @Query("select wallet from Deal where pid=(select max(d.pid) from Deal d where d.user.chatId=:chatId and d.isPassed=true and d.dealType=:dealType)")
    String getWalletFromLastPassedByChatIdAndDealType(Long chatId, DealType dealType);

    @Query("select wallet from Deal where pid=(select max(d.pid) from Deal d where d.user.chatId=:chatId and d.isPassed=true " +
            "and d.dealType=:dealType and d.cryptoCurrency=:cryptoCurrency)")
    String getWalletFromLastPassedByChatIdAndDealTypeAndCryptoCurrency(Long chatId, DealType dealType, CryptoCurrency cryptoCurrency);

    Deal findByPid(Long pid);

    @Query("select amount from Deal where pid=:pid")
    BigDecimal getAmountByPid(Long pid);

    @Query("select cryptoAmount from Deal where pid=:pid")
    BigDecimal getCryptoAmountByPid(Long pid);

    @Query("select discount from Deal where pid=:pid")
    BigDecimal getDiscountByPid(Long pid);

    @Query("select d.pid from Deal d where d.user.chatId=:chatId and d.isActive=true")
    Long getPidActiveDealByChatId(Long chatId);

    @Query("select count(d) from Deal d where d.isPassed=true and d.user.chatId=:chatId")
    Long getCountPassedByUserChatId(Long chatId);

    @Query("select pid from Deal where isActive=true")
    List<Long> getActiveDealPids();

    @Query("select d.user.chatId from Deal d where d.pid=:pid")
    Long getUserChatIdByDealPid(Long pid);

    @Query("select d.user.username from Deal d where d.pid=:pid")
    String getUserUsernameByDealPid(Long pid);

    @Query("from Deal d where (d.date BETWEEN :startDate AND :endDate) and d.isPassed=true")
    List<Deal> getByDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("from Deal d where d.date=:date and d.isPassed=true")
    List<Deal> getPassedByDate(LocalDate date);

    @Query("select dealType from Deal where pid=:pid")
    DealType getDealTypeByPid(Long pid);

    @Query(value = "select dateTime from Deal where pid=:pid")
    LocalDateTime getDateTimeByPid(Long pid);

    @Query(value = "select fiatCurrency from Deal where pid=:pid")
    FiatCurrency getFiatCurrencyByPid(Long pid);

    @Query(value = "select additionalVerificationImageId from Deal where pid=:pid")
    String getAdditionalVerificationImageIdByPid(Long pid);

    @Query(value = "select deliveryType from Deal where pid=:pid")
    DeliveryType getDeliveryTypeByPid(Long pid);

    @Query("select creditedAmount from Deal where pid=:pid")
    BigDecimal getCreditedAmountByPid(Long pid);

    /**
     * Reports
     */

    @Query(value = "select sum(cryptoAmount) from Deal where isPassed=:isPassed and dealType=:dealType and date=:date and cryptoCurrency=:cryptoCurrency")
    BigDecimal getCryptoAmountSum(boolean isPassed, DealType dealType, LocalDate date, CryptoCurrency cryptoCurrency);

    @Query(value = "select sum(cryptoAmount) from Deal where isPassed=:isPassed and dealType=:dealType and (dateTime between :dateFrom and :dateTo) and cryptoCurrency=:cryptoCurrency")
    BigDecimal getCryptoAmountSum(boolean isPassed, DealType dealType, LocalDateTime dateFrom, LocalDateTime dateTo, CryptoCurrency cryptoCurrency);

    @Query(value = "select sum(amount) from Deal where isPassed=:isPassed and dealType=:dealType and dateTime=:dateTime and cryptoCurrency=:cryptoCurrency")
    BigDecimal getTotalAmountSum(boolean isPassed, DealType dealType, LocalDateTime dateTime, CryptoCurrency cryptoCurrency);

    @Query(value = "select sum(amount) from Deal where isPassed=:isPassed and dealType=:dealType and dateTime=:dateTime and cryptoCurrency=:cryptoCurrency and fiatCurrency=:fiatCurrency")
    BigDecimal getTotalAmountSum(boolean isPassed, DealType dealType, LocalDateTime dateTime, CryptoCurrency cryptoCurrency, FiatCurrency fiatCurrency);


    @Query(value = "select sum(amount) from Deal where isPassed=:isPassed and dealType=:dealType and (dateTime between :dateFrom and :dateTo) and cryptoCurrency=:cryptoCurrency")
    BigDecimal getTotalAmountSum(boolean isPassed, DealType dealType, LocalDateTime dateFrom, LocalDateTime dateTo, CryptoCurrency cryptoCurrency);

    @Query(value = "select sum(amount) from Deal where isPassed=:isPassed and dealType=:dealType and (dateTime between :dateFrom and :dateTo) and cryptoCurrency=:cryptoCurrency and fiatCurrency=:fiatCurrency")
    BigDecimal getTotalAmountSum(boolean isPassed, DealType dealType, LocalDateTime dateFrom, LocalDateTime dateTo, CryptoCurrency cryptoCurrency, FiatCurrency fiatCurrency);

    @Query(value = "select count(pid) from Deal where user.chatId=:chatId and isPassed=true")
    Integer getCountPassedByChatId(Long chatId);

    @Query(value = "select count(pid) from Deal where dateTime between :startDateTime and :endDateTime and isPassed=true")
    Integer getCountByPeriod(LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query(value = "select sum(cryptoAmount) from Deal where user.chatId=:chatId and isPassed=true and cryptoCurrency=:cryptoCurrency and dealType=:dealType")
    BigDecimal getUserCryptoAmountSum(Long chatId, CryptoCurrency cryptoCurrency, DealType dealType);

    @Query(value = "select sum(amount) from Deal where user.chatId=:chatId and isPassed=true and dealType=:dealType")
    BigDecimal getUserAmountSum(Long chatId, DealType dealType);

    @Query(value = "select sum(amount) from Deal where user.chatId=:chatId and isPassed=true and dealType=:dealType and fiatCurrency=:fiatCurrency")
    BigDecimal getUserAmountSumByDealTypeAndFiatCurrency(Long chatId, DealType dealType, FiatCurrency fiatCurrency);

    List<Deal> getByUser_ChatId(Long chatId);

    @Query(value = "select isActive from Deal where pid=:pid")
    Boolean getIsActiveByPid(Long pid);

    @Query(value = "select isUsedPromo from Deal where pid=:pid")
    Boolean getIsUsedPromoByPid(Long pid);

    @Query(value = "select pid,user.pid,dealType,cryptoCurrency,cryptoAmount,fiatCurrency,amount from Deal where isPassed=true")
    List<Object[]> findAllForUsersReport();

    @Query(value = "select dealStatus from Deal where pid=:pid")
    DealStatus getDealStatusByPid(Long pid);
}
