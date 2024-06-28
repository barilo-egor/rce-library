package tgb.btc.library.service.bean.bot;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tgb.btc.library.bean.bot.PaymentRequisite;
import tgb.btc.library.bean.bot.PaymentType;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.service.bot.IPaymentRequisiteService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.PaymentRequisiteRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PaymentRequisiteService extends BasePersistService<PaymentRequisite> implements IPaymentRequisiteService {

    private PaymentRequisiteRepository paymentRequisiteRepository;

    private final Map<Long, Integer> PAYMENT_REQUISITE_ORDER = new HashMap<>();

    @Autowired
    public PaymentRequisiteService(BaseRepository<PaymentRequisite> baseRepository) {
        super(baseRepository);
    }

    @Autowired
    public void setPaymentRequisiteRepository(PaymentRequisiteRepository paymentRequisiteRepository) {
        this.paymentRequisiteRepository = paymentRequisiteRepository;
    }

    private Integer getOrder(Long paymentTypePid) {
        synchronized (this) {
            Integer order = PAYMENT_REQUISITE_ORDER.get(paymentTypePid);
            if (Objects.isNull(order)) {
                order = 0;
                PAYMENT_REQUISITE_ORDER.put(paymentTypePid, order);
            }
            return order;
        }
    }

    public void updateOrder(Long paymentTypePid) {
        synchronized (this) {
            Integer order = PAYMENT_REQUISITE_ORDER.get(paymentTypePid);
            if (Objects.isNull(order)) {
                order = 0;
                PAYMENT_REQUISITE_ORDER.put(paymentTypePid, order);
            } else {
                Integer paymentTypeRequisitesSize = paymentRequisiteRepository.countByPaymentTypePidAndIsOn(paymentTypePid);
                if (Objects.isNull(paymentTypeRequisitesSize) || paymentTypeRequisitesSize.equals(order + 1))
                    PAYMENT_REQUISITE_ORDER.put(paymentTypePid, 0);
                else PAYMENT_REQUISITE_ORDER.put(paymentTypePid, order + 1);
            }
        }
    }

    public void removeOrder(Long paymentTypePid) {
        synchronized (this) {
            PAYMENT_REQUISITE_ORDER.remove(paymentTypePid);
        }
    }

    public String getRequisite(PaymentType paymentType) {
        List<PaymentRequisite> paymentRequisite = paymentRequisiteRepository.getByPaymentType_Pid(paymentType.getPid());
        if (CollectionUtils.isEmpty(paymentRequisite)) {
            throw new BaseException("Не установлены реквизиты для " + paymentType.getName() + ".");
        }
        if (BooleanUtils.isNotTrue(paymentType.getDynamicOn())) {
            return paymentRequisite.stream()
                    .filter(requisite -> BooleanUtils.isTrue(requisite.getOn()))
                    .findFirst()
                    .orElseThrow(() -> new BaseException("Не найден ни один включенный реквизит"))
                    .getRequisite();
        }
        List<PaymentRequisite> turnedRequisites = paymentRequisite.stream()
                .filter(requisite -> BooleanUtils.isTrue(requisite.getOn()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(turnedRequisites))
            throw new BaseException("Не найден ни один включенный реквизит.");
        Integer order = getOrder(paymentType.getPid());
        return turnedRequisites.get(order).getRequisite();
    }

    @Override
    public List<PaymentRequisite> getByPaymentType(PaymentType paymentType) {
        return paymentRequisiteRepository.getByPaymentType(paymentType);
    }

    @Override
    public long getCountByPaymentType(PaymentType paymentType) {
        return paymentRequisiteRepository.getCountByPaymentType(paymentType);
    }

    @Override
    public List<PaymentRequisite> getByPaymentType_Pid(Long paymentTypePid) {
        return paymentRequisiteRepository.getByPaymentType_Pid(paymentTypePid);
    }

    @Override
    public PaymentType getPaymentTypeByPid(Long pid) {
        return paymentRequisiteRepository.getPaymentTypeByPid(pid);
    }

    @Override
    public String getRequisiteByPaymentTypePidAndOrder(Long paymentPid, Integer requisiteOrder) {
        return paymentRequisiteRepository.getRequisiteByPaymentTypePidAndOrder(paymentPid, requisiteOrder);
    }

    @Override
    public Integer countByPaymentTypePidAndIsOn(Long paymentTypePid) {
        return paymentRequisiteRepository.countByPaymentTypePidAndIsOn(paymentTypePid);
    }

    @Override
    public void updateRequisiteByPid(String requisite, Long pid) {
        paymentRequisiteRepository.updateRequisiteByPid(requisite, pid);
    }

    @Override
    public void deleteByPaymentTypePid(Long paymentTypePid) {
        paymentRequisiteRepository.deleteByPaymentTypePid(paymentTypePid);
    }
}
