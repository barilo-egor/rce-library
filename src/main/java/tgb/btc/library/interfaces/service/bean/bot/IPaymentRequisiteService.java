package tgb.btc.library.interfaces.service.bean.bot;

import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.PaymentRequisite;
import tgb.btc.library.bean.bot.PaymentType;
import tgb.btc.library.interfaces.service.IBasePersistService;

import java.util.List;

public interface IPaymentRequisiteService extends IBasePersistService<PaymentRequisite> {

    String getRequisite(Deal deal);

    List<PaymentRequisite> getByPaymentType(PaymentType paymentType);

    long getCountByPaymentType(PaymentType paymentType);

    List<PaymentRequisite> getByPaymentType_Pid(Long paymentTypePid);

    PaymentType getPaymentTypeByPid(Long pid);

    Integer countByPaymentTypePidAndIsOn(Long paymentTypePid);

    void updateRequisiteByPid(String requisite, Long pid);

    void deleteByPaymentTypePid(Long paymentTypePid);

    String getRequisite(PaymentType paymentType);

    void checkOrder(Long paymentTypePid);

    void updateOrder(Long paymentTypePid);

    void removeOrder(Long paymentTypePid);
}
