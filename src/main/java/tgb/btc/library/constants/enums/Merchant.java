package tgb.btc.library.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tgb.btc.library.bean.bot.PaymentType;
import tgb.btc.library.constants.enums.properties.VariableType;
import tgb.btc.library.constants.enums.web.merchant.alfateam.InvoiceStatus;
import tgb.btc.library.constants.enums.web.merchant.alfateam.PaymentOption;
import tgb.btc.library.constants.enums.web.merchant.dashpay.DashPayOrderStatus;
import tgb.btc.library.constants.enums.web.merchant.dashpay.OrderMethod;
import tgb.btc.library.constants.enums.web.merchant.evopay.EvoPayPaymentMethod;
import tgb.btc.library.constants.enums.web.merchant.evopay.EvoPayStatus;
import tgb.btc.library.constants.enums.web.merchant.honeymoney.HoneyMoneyMethod;
import tgb.btc.library.constants.enums.web.merchant.honeymoney.HoneyMoneyStatus;
import tgb.btc.library.constants.enums.web.merchant.nicepay.NicePayMethod;
import tgb.btc.library.constants.enums.web.merchant.nicepay.NicePayStatus;
import tgb.btc.library.constants.enums.web.merchant.onlypays.OnlyPaysPaymentType;
import tgb.btc.library.constants.enums.web.merchant.onlypays.OnlyPaysStatus;
import tgb.btc.library.constants.enums.web.merchant.payfinity.PayFinityOrderType;
import tgb.btc.library.constants.enums.web.merchant.payfinity.PayFinityStatus;
import tgb.btc.library.constants.enums.web.merchant.paypoints.PayPointsMethod;
import tgb.btc.library.constants.enums.web.merchant.paypoints.PayPointsStatus;
import tgb.btc.library.constants.enums.web.merchant.payscrow.OrderStatus;
import tgb.btc.library.constants.enums.web.merchant.payscrow.PaymentMethodType;
import tgb.btc.library.constants.enums.web.merchant.wellbit.WellBitMethod;
import tgb.btc.library.constants.enums.web.merchant.wellbit.WellBitStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum Merchant {
    NONE(null, "none", false, new HashMap<>(), paymentType -> false,
            paymentType -> null, (paymentType, s) -> {}, (paymentType -> {}),
            name -> ""),
    PAYSCROW(VariableType.PAYSCROW_BOUND, "Payscrow", true,
            Arrays.stream(PaymentMethodType.values()).collect(Collectors.toMap(Enum::name, PaymentMethodType::getDescription)),
            paymentType -> Objects.nonNull(paymentType.getPayscrowPaymentMethodId()),
            paymentType -> Objects.requireNonNull(PaymentMethodType.fromMethodId(paymentType.getPayscrowPaymentMethodId())).getDescription(),
            (paymentType, name) -> paymentType.setPayscrowPaymentMethodId(PaymentMethodType.valueOf(name).getMethodId()),
            (paymentType) -> paymentType.setPayscrowPaymentMethodId(null),
            name -> OrderStatus.valueOf(name).getDescription()
    ),
    DASH_PAY(VariableType.DASH_PAY_BOUND, "DashPay", true,
            Arrays.stream(OrderMethod.values()).collect(Collectors.toMap(Enum::name, OrderMethod::getDisplayName)),
            paymentType -> Objects.nonNull(paymentType.getDashPayOrderMethod()),
            paymentType -> paymentType.getDashPayOrderMethod().getDisplayName(),
            ((paymentType, name) -> paymentType.setDashPayOrderMethod(OrderMethod.valueOf(name))),
            (paymentType) -> paymentType.setDashPayOrderMethod(null),
            name -> DashPayOrderStatus.valueOf(name).getDescription()
    ),
    ALFA_TEAM(VariableType.ALFA_TEAM_BOUND, "AlfaTeam", true,
            Arrays.stream(PaymentOption.values()).collect(Collectors.toMap(Enum::name, PaymentOption::getDescription)),
            paymentType -> Objects.nonNull(paymentType.getAlfaTeamPaymentOption()),
            paymentType -> paymentType.getAlfaTeamPaymentOption().getDescription(),
            ((paymentType, name) -> paymentType.setAlfaTeamPaymentOption(PaymentOption.valueOf(name))),
            (paymentType) -> paymentType.setAlfaTeamPaymentOption(null),
            name -> InvoiceStatus.valueOf(name).getDescription()

    ),
    ALFA_TEAM_TJS(VariableType.ALFA_TEAM_TJS_BOUND, "AlfaTeam TJS", true,
            Arrays.stream(PaymentOption.values()).collect(Collectors.toMap(Enum::name, PaymentOption::getDescription)),
            paymentType -> Objects.nonNull(paymentType.getAlfaTeamTJSPaymentOption()),
            paymentType -> paymentType.getAlfaTeamTJSPaymentOption().getDescription(),
            ((paymentType, name) -> paymentType.setAlfaTeamTJSPaymentOption(PaymentOption.valueOf(name))),
            (paymentType) -> paymentType.setAlfaTeamTJSPaymentOption(null),
            name -> InvoiceStatus.valueOf(name).getDescription()
    ),
    ALFA_TEAM_VTB(VariableType.ALFA_TEAM_VTB_BOUND, "AlfaTeam VTB", true,
            Arrays.stream(PaymentOption.values()).collect(Collectors.toMap(Enum::name, PaymentOption::getDescription)),
            paymentType -> Objects.nonNull(paymentType.getAlfaTeamVTBPaymentOption()),
            paymentType -> paymentType.getAlfaTeamVTBPaymentOption().getDescription(),
            ((paymentType, name) -> paymentType.setAlfaTeamVTBPaymentOption(PaymentOption.valueOf(name))),
            (paymentType) -> paymentType.setAlfaTeamVTBPaymentOption(null),
            name -> InvoiceStatus.valueOf(name).getDescription()
    ),
    ALFA_TEAM_ALFA(VariableType.ALFA_TEAM_ALFA_BOUND, "AlfaTeam ALFA", true,
            Arrays.stream(PaymentOption.values()).collect(Collectors.toMap(Enum::name, PaymentOption::getDescription)),
            paymentType -> Objects.nonNull(paymentType.getAlfaTeamAlfaPaymentOption()),
            paymentType -> paymentType.getAlfaTeamAlfaPaymentOption().getDescription(),
            ((paymentType, name) -> paymentType.setAlfaTeamAlfaPaymentOption(PaymentOption.valueOf(name))),
            (paymentType) -> paymentType.setAlfaTeamAlfaPaymentOption(null),
            name -> InvoiceStatus.valueOf(name).getDescription()
    ),
    ALFA_TEAM_SBER(VariableType.ALFA_TEAM_SBER_BOUND, "AlfaTeam SBER", true,
            Arrays.stream(PaymentOption.values()).collect(Collectors.toMap(Enum::name, PaymentOption::getDescription)),
            paymentType -> Objects.nonNull(paymentType.getAlfaTeamSberPaymentOption()),
            paymentType -> paymentType.getAlfaTeamSberPaymentOption().getDescription(),
            ((paymentType, name) -> paymentType.setAlfaTeamSberPaymentOption(PaymentOption.valueOf(name))),
            (paymentType) -> paymentType.setAlfaTeamSberPaymentOption(null),
            name -> InvoiceStatus.valueOf(name).getDescription()
    ),
    PAY_POINTS(VariableType.PAY_POINTS_BOUND, "Paypoints", false,
            Arrays.stream(PayPointsMethod.values()).collect(Collectors.toMap(Enum::name, PayPointsMethod::getDisplayName)),
            paymentType -> Objects.nonNull(paymentType.getPayPointsMethod()),
            paymentType -> paymentType.getPayPointsMethod().getDisplayName(),
            ((paymentType, name) -> paymentType.setPayPointsMethod(PayPointsMethod.valueOf(name))),
            (paymentType) -> paymentType.setPayPointsMethod(null),
            name -> PayPointsStatus.valueOf(name).getDisplayName()
    ),
    ONLY_PAYS(VariableType.ONLY_PAYS_BOUND, "OnlyPays", false,
            Arrays.stream(OnlyPaysPaymentType.values()).collect(Collectors.toMap(Enum::name, OnlyPaysPaymentType::getDescription)),
            paymentType -> Objects.nonNull(paymentType.getOnlyPaysPaymentType()),
            paymentType -> paymentType.getOnlyPaysPaymentType().getDescription(),
            ((paymentType, name) -> paymentType.setOnlyPaysPaymentType(OnlyPaysPaymentType.valueOf(name))),
            (paymentType) -> paymentType.setOnlyPaysPaymentType(null),
            name -> OnlyPaysStatus.valueOf(name).getDescription()
    ),
    EVO_PAY(VariableType.EVO_PAY_BOUND, "EvoPay", false,
            Arrays.stream(EvoPayPaymentMethod.values()).collect(Collectors.toMap(Enum::name, EvoPayPaymentMethod::getDescription)),
            paymentType -> Objects.nonNull(paymentType.getEvoPayPaymentMethod()),
            paymentType -> paymentType.getEvoPayPaymentMethod().getDescription(),
            ((paymentType, name) -> paymentType.setEvoPayPaymentMethod(EvoPayPaymentMethod.valueOf(name))),
            (paymentType) -> paymentType.setEvoPayPaymentMethod(null),
            name -> EvoPayStatus.valueOf(name).getDescription()
    ),
    NICE_PAY(VariableType.NICE_PAY_BOUND, "NicePay", false,
            Arrays.stream(NicePayMethod.values()).collect(Collectors.toMap(Enum::name, NicePayMethod::getDescription)),
            paymentType -> Objects.nonNull(paymentType.getNicePayMethod()),
            paymentType -> paymentType.getNicePayMethod().getDescription(),
            ((paymentType, name) -> paymentType.setNicePayMethod(NicePayMethod.valueOf(name))),
            (paymentType) -> paymentType.setNicePayMethod(null),
            name -> NicePayStatus.valueOf(name).getDescription()
    ),
    HONEY_MONEY(VariableType.HONEY_MONEY_BOUND, "HoneyMoney", false,
            Arrays.stream(HoneyMoneyMethod.values()).collect(Collectors.toMap(Enum::name, HoneyMoneyMethod::getDescription)),
            paymentType -> Objects.nonNull(paymentType.getHoneyMoneyMethod()),
            paymentType -> paymentType.getHoneyMoneyMethod().getDescription(),
            ((paymentType, name) -> paymentType.setHoneyMoneyMethod(HoneyMoneyMethod.valueOf(name))),
            (paymentType) -> paymentType.setHoneyMoneyMethod(null),
            name -> HoneyMoneyStatus.valueOf(name).getDescription()
    ),
    PAY_FINITY(VariableType.PAY_FINITY_BOUND, "PayFinity", false,
            Arrays.stream(PayFinityOrderType.values()).collect(Collectors.toMap(Enum::name, PayFinityOrderType::getDescription)),
            paymentType -> Objects.nonNull(paymentType.getPayFinityOrderType()),
            paymentType -> paymentType.getPayFinityOrderType().getDescription(),
            ((paymentType, name) -> paymentType.setPayFinityOrderType(PayFinityOrderType.valueOf(name))),
            (paymentType) -> paymentType.setPayFinityOrderType(null),
            name -> PayFinityStatus.valueOf(name).getDescription()
    ),
    WELL_BIT(VariableType.WELL_BIT_BOUND, "WellBit", false,
            Arrays.stream(WellBitMethod.values()).collect(Collectors.toMap(Enum::name, WellBitMethod::getDescription)),
            paymentType -> Objects.nonNull(paymentType.getWellBitMethod()),
            paymentType -> paymentType.getWellBitMethod().getDescription(),
            ((paymentType, name) -> paymentType.setWellBitMethod(WellBitMethod.valueOf(name))),
            (paymentType) -> paymentType.setWellBitMethod(null),
            name -> WellBitStatus.valueOf(name).getDescription()
    )
    ;

    private final VariableType maxAmount;

    private final String displayName;

    private final boolean isDecimalAmount;

    private final Map<String, String> methodDescriptions;

    private final Predicate<PaymentType> hasBindPredicate;

    private final Function<PaymentType, String> getMethodDescriptionFunction;

    private final BiConsumer<PaymentType, String> setMethodFromNameConsumer;

    private final Consumer<PaymentType> setEmptyMethodConsumer;

    private final Function<String, String> getDisplayStatusFunction;

    public String getBindingButtonText() {
        return this.displayName + " привязка";
    }
}
