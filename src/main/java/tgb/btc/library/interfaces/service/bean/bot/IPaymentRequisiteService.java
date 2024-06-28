package tgb.btc.library.interfaces.service.bean.bot;

import tgb.btc.library.bean.bot.PaymentRequisite;
import tgb.btc.library.bean.bot.PaymentType;
import tgb.btc.library.interfaces.service.IBasePersistService;

import java.util.List;

public interface IPaymentRequisiteService extends IBasePersistService<PaymentRequisite> {

    List<PaymentRequisite> getByPaymentType(PaymentType paymentType);

    long getCountByPaymentType(PaymentType paymentType);

    List<PaymentRequisite> getByPaymentType_Pid(Long paymentTypePid);

    PaymentType getPaymentTypeByPid(Long pid);

    String getRequisiteByPaymentTypePidAndOrder(Long paymentPid, Integer requisiteOrder);

    Integer countByPaymentTypePidAndIsOn(Long paymentTypePid);

    void updateRequisiteByPid(String requisite, Long pid);

    void deleteByPaymentTypePid(Long paymentTypePid);
}
