package tgb.btc.library.service.bean.bot;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.PaymentRequisite;
import tgb.btc.library.bean.bot.PaymentType;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.constants.enums.properties.VariableType;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.service.bean.bot.IPaymentRequisiteService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.PaymentRequisiteRepository;
import tgb.btc.library.service.bean.BasePersistService;
import tgb.btc.library.service.properties.VariablePropertiesReader;
import tgb.btc.library.service.web.merchant.IMerchantRequisiteService;
import tgb.btc.library.vo.RequisiteVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@Transactional
public class PaymentRequisiteService extends BasePersistService<PaymentRequisite> implements IPaymentRequisiteService {

    private final PaymentRequisiteRepository paymentRequisiteRepository;

    private final VariablePropertiesReader variablePropertiesReader;

    private final Map<Long, Integer> PAYMENT_REQUISITE_ORDER = new HashMap<>();

    private final Map<Merchant, IMerchantRequisiteService> merchantIMerchantRequisiteServiceMap;

    public PaymentRequisiteService(PaymentRequisiteRepository paymentRequisiteRepository,
                                   VariablePropertiesReader variablePropertiesReader,
                                   List<IMerchantRequisiteService> merchantRequisiteServices) {
        this.paymentRequisiteRepository = paymentRequisiteRepository;
        this.variablePropertiesReader = variablePropertiesReader;
        this.merchantIMerchantRequisiteServiceMap = new HashMap<>();
        for (IMerchantRequisiteService merchantService : merchantRequisiteServices) {
            this.merchantIMerchantRequisiteServiceMap.put(merchantService.getMerchant(), merchantService);
        }
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
            return RequisiteVO.builder().merchant(Merchant.NONE).requisite(getRequisite(deal.getPaymentType())).build();
        }
        RequisiteVO requisiteVO = null;
        List<String> merchants = variablePropertiesReader.getStringList("merchant.list");
        int attemptsCount = variablePropertiesReader.getInt(VariableType.NUMBER_OF_MERCHANT_ATTEMPTS, 1);
        int attemptsDelay = variablePropertiesReader.getInt(VariableType.DELAY_MERCHANT_ATTEMPTS, 3);
        for (int i = 0; i < attemptsCount; i++) {
            for (String merchantName : merchants) {
                try {
                    Merchant merchant = Merchant.valueOf(merchantName);
                    Long maxAmount = variablePropertiesReader.getLong(merchant.getMaxAmount().getKey(), 5000L);
                    if (deal.getAmount().longValue() > maxAmount) {
                        continue;
                    }
                    requisiteVO = merchantIMerchantRequisiteServiceMap.get(merchant).getRequisite(deal);
                    if (Objects.nonNull(requisiteVO)) break;
                } catch (Exception e) {
                    log.debug("Ошибка получения реквизитов мерчанта {}.", merchantName, e);
                }
            }
            if (Objects.nonNull(requisiteVO)) break;
            try {
                Thread.sleep(attemptsDelay * 1000L);
            } catch (InterruptedException ignored) {
            }
        }
        if (Objects.isNull(requisiteVO)) {
            requisiteVO =  RequisiteVO.builder().merchant(Merchant.NONE).requisite(getRequisite(deal.getPaymentType())).build();
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
