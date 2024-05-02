package tgb.btc.library.repository.bot;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.PaymentRequisite;
import tgb.btc.library.bean.bot.PaymentType;
import tgb.btc.library.repository.BaseRepository;

import java.util.List;

@Repository
@Transactional
public interface PaymentRequisiteRepository extends BaseRepository<PaymentRequisite> {

    @Query("from PaymentRequisite where paymentType=:paymentType")
    List<PaymentRequisite> getByPaymentType(PaymentType paymentType);

    @Query("select count(pid) from PaymentRequisite where paymentType=:paymentType")
    long getCountByPaymentType(PaymentType paymentType);

    List<PaymentRequisite> getByPaymentType_Pid(Long paymentTypePid);

    @Query("select pr.paymentType from PaymentRequisite pr where pr.pid=:pid")
    PaymentType getPaymentTypeByPid(Long pid);

    @Query("select requisite from PaymentRequisite where paymentType.pid=:paymentPid and requisiteOrder=:requisiteOrder")
    String getRequisiteByPaymentTypePidAndOrder(Long paymentPid, Integer requisiteOrder);

    @Query("select count(pid) from PaymentRequisite where paymentType.pid=:paymentTypePid and isOn = true")
    Integer countByPaymentTypePidAndIsOn(Long paymentTypePid);

    @Modifying
    @Query("update PaymentRequisite set requisite=:requisite where pid=:pid")
    void updateRequisiteByPid(String requisite, Long pid);

    @Modifying
    @Query("delete from PaymentRequisite where paymentType.pid=:paymentTypePid")
    void deleteByPaymentTypePid(Long paymentTypePid);
}
