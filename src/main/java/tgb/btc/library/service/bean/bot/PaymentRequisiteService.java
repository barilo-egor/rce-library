package tgb.btc.library.service.bean.bot;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.PaymentRequisite;
import tgb.btc.library.bean.bot.PaymentType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.service.bean.bot.IPaymentRequisiteService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.PaymentRequisiteRepository;
import tgb.btc.library.service.bean.BasePersistService;
import tgb.btc.library.service.web.merchant.MerchantRequisiteService;
import tgb.btc.library.vo.RequisiteVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class PaymentRequisiteService extends BasePersistService<PaymentRequisite> implements IPaymentRequisiteService {

    private final PaymentRequisiteRepository paymentRequisiteRepository;

    private final Map<Long, Integer> PAYMENT_REQUISITE_ORDER = new HashMap<>();

    private final MerchantRequisiteService merchantRequisiteService;

    public PaymentRequisiteService(PaymentRequisiteRepository paymentRequisiteRepository,
                                   MerchantRequisiteService merchantRequisiteService) {
        this.paymentRequisiteRepository = paymentRequisiteRepository;
        this.merchantRequisiteService = merchantRequisiteService;
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

    @Override
    public void checkOrder(Long paymentTypePid) {
        synchronized (this) {
            Integer order = PAYMENT_REQUISITE_ORDER.get(paymentTypePid);
            if (Objects.isNull(order)) {
                order = 0;
                PAYMENT_REQUISITE_ORDER.put(paymentTypePid, order);
            } else {
                Integer paymentTypeRequisitesSize = paymentRequisiteRepository.countByPaymentTypePidAndIsOn(paymentTypePid);
                if (Objects.isNull(paymentTypeRequisitesSize) || order >= paymentTypeRequisitesSize) {
                    PAYMENT_REQUISITE_ORDER.put(paymentTypePid, 0);
                }
            }
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
                if (Objects.isNull(paymentTypeRequisitesSize) || order + 1 >= paymentTypeRequisitesSize)
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

    @Override
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
                .toList();
        if (CollectionUtils.isEmpty(turnedRequisites))
            throw new BaseException("Не найден ни один включенный реквизит.");
        Integer order = getOrder(paymentType.getPid());
        updateOrder(paymentType.getPid());
        return turnedRequisites.get(order).getRequisite();
    }

    @Override
    public RequisiteVO getRequisite(Deal deal) {
        if (!FiatCurrency.RUB.equals(deal.getFiatCurrency())) {
            return RequisiteVO.builder().requisite(getRequisite(deal.getPaymentType())).build();
        }
        RequisiteVO requisiteVO = merchantRequisiteService.getRequisites(deal);
        if (Objects.isNull(requisiteVO)) {
            requisiteVO =  RequisiteVO.builder().requisite(getRequisite(deal.getPaymentType())).build();
        }
        return requisiteVO;
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

    @Override
    protected BaseRepository<PaymentRequisite> getBaseRepository() {
        return paymentRequisiteRepository;
    }

}
