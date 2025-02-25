package tgb.btc.library.service.bean.bot;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.PaymentRequisite;
import tgb.btc.library.bean.bot.PaymentType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.constants.enums.properties.VariableType;
import tgb.btc.library.constants.enums.web.merchant.payscrow.PaymentMethodType;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.service.bean.bot.IPaymentRequisiteService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.PaymentRequisiteRepository;
import tgb.btc.library.repository.bot.deal.ModifyDealRepository;
import tgb.btc.library.service.bean.BasePersistService;
import tgb.btc.library.service.properties.VariablePropertiesReader;
import tgb.btc.library.service.web.merchant.payscrow.PayscrowMerchantService;
import tgb.btc.library.vo.web.merchant.payscrow.PayscrowOrderResponse;

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

    private final PayscrowMerchantService payscrowMerchantService;

    private final ModifyDealRepository modifyDealRepository;

    private final Map<Long, Integer> PAYMENT_REQUISITE_ORDER = new HashMap<>();

    public PaymentRequisiteService(PaymentRequisiteRepository paymentRequisiteRepository,
                                   VariablePropertiesReader variablePropertiesReader,
                                   PayscrowMerchantService payscrowMerchantService,
                                   ModifyDealRepository modifyDealRepository) {
        this.paymentRequisiteRepository = paymentRequisiteRepository;
        this.variablePropertiesReader = variablePropertiesReader;
        this.payscrowMerchantService = payscrowMerchantService;
        this.modifyDealRepository = modifyDealRepository;
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
    public String getRequisite(Deal deal) {
        if (!FiatCurrency.RUB.equals(deal.getFiatCurrency()) || Objects.isNull(deal.getPaymentType().getPayscrowPaymentMethodId())) {
            return getRequisite(deal.getPaymentType());
        }
        Long maxAmount = variablePropertiesReader.getLong(VariableType.PAYSCROW_BOUND.getKey(), 5000L);
        if (deal.getAmount().longValue() > maxAmount) {
            return getRequisite(deal.getPaymentType());
        }
        PayscrowOrderResponse payscrowOrderResponse;
        try {
            payscrowOrderResponse = payscrowMerchantService.createBuyOrder(deal);
        } catch (Exception e) {
            log.error("Ошибка при выполнении запроса на создание Payscrow ордера.", e);
            return getRequisite(deal.getPaymentType());
        }
        if (!payscrowOrderResponse.getSuccess()) {
            log.warn("Неуспешный ответ от Payscrow при создании Buy ордера для сделки №{}: {}", deal.getPid(), payscrowOrderResponse);
            return getRequisite(deal.getPaymentType());
        }
        deal.setPayscrowOrderId(payscrowOrderResponse.getOrderId());
        modifyDealRepository.save(deal);
        return buildRequisiteString(payscrowOrderResponse);
    }

    private static String buildRequisiteString(PayscrowOrderResponse payscrowOrderResponse) {
        String result;
        String holderAccount = payscrowOrderResponse.getHolderAccount();
        if (PaymentMethodType.BANK_CARD.equals(payscrowOrderResponse.getPaymentMethodType())) {
            StringBuilder card = new StringBuilder();
            for (int i = 0; i < holderAccount.length(); i++) {
                card.append(holderAccount.charAt(i));

                if ((i + 1) % 4 == 0 && i != holderAccount.length() - 1) {
                    card.append(" ");
                }
            }
            result = payscrowOrderResponse.getMethodName() + " " + card;
        } else {
            result = payscrowOrderResponse.getMethodName() + " " + holderAccount;
        }
        return result;
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
