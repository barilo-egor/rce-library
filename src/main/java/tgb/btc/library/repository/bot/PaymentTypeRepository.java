package tgb.btc.library.repository.bot;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.PaymentType;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.repository.BaseRepository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@Transactional
public interface PaymentTypeRepository extends BaseRepository<PaymentType> {

    /** SELECT **/

    @Query("from PaymentType where pid=:pid")
    PaymentType getByPid(@Param("pid") Long pid);

    @Query("from PaymentType where dealType=:dealType")
    List<PaymentType> getByDealType(DealType dealType);

    List<PaymentType> getByDealTypeAndFiatCurrency(DealType dealType,  FiatCurrency fiatCurrency);

    @Query("select count(pid) from PaymentType where dealType=:dealType and fiatCurrency=:fiatCurrency")
    long countByDealTypeAndFiatCurrency(DealType dealType,  FiatCurrency fiatCurrency);

    List<PaymentType> getByDealTypeAndIsOnAndFiatCurrency(DealType dealType, Boolean isOn, FiatCurrency fiatCurrency);

    @Query("select count(pid) from PaymentType where dealType=:dealType and isOn=:isOn and fiatCurrency=:fiatCurrency")
    Integer countByDealTypeAndIsOnAndFiatCurrency(DealType dealType, Boolean isOn, FiatCurrency fiatCurrency);

    @Query("select dealType from PaymentType where pid=:pid")
    DealType getDealTypeByPid(Long pid);

    long countByNameLike(String name);


    /** UPDATE **/
    @Modifying
    @Query("update PaymentType set isOn=:isOn where pid=:pid")
    void updateIsOnByPid(Boolean isOn, Long pid);

    @Modifying
    @Query("update PaymentType set minSum=:minSum where pid=:pid")
    void updateMinSumByPid(BigDecimal minSum, Long pid);

    @Modifying
    @Query("update PaymentType set isDynamicOn=:isDynamicOn where pid=:pid")
    void updateIsDynamicOnByPid(Boolean isDynamicOn, Long pid);
}
