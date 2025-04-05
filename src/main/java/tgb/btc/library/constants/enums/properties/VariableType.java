package tgb.btc.library.constants.enums.properties;

import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealType;

public enum VariableType {
    USD_COURSE("Курс доллара", "course.usd"),
    FIX("Фикс рублей покупка", "fix"),
    FIX_COMMISSION("Фикс комиссия", "commission.fix"),
    COMMISSION("Комиссия", "commission"),
    OPERATOR_LINK("Ссылка на оператора", "operator.link"),
    PROBABILITY("Шанс лотереи", "lottery.chance"),
    MIN_SUM("Мин.сумма пок.", "deal.min.sum"),
    TRANSACTION_COMMISSION("Транз.комиссия", "transaction.commission"),
    PROMO_CODE_DISCOUNT("Скидка от промокода", "promo.code.discount"),
    PROMO_CODE_NAME("Название промокода", "promo.code.name"),
    DEAL_ACTIVE_TIME("Время активности заявки", "deal.active.time"),
    WALLET("Кошелек BTC", "wallet"),
    REFERRAL_PERCENT("Процент рефералов", "referral.percent"),
    REFERRAL_MIN_SUM("Мин.сумма вывода", "referral.min.sum"),
    CHANNEL_CHAT_ID("Айди канала", "channel.chat.id"),
    REVIEW_PRISE("Вознаграждение", "review.prise"),
    USDT_COURSE("Курс USDT", "usdt.course"),
    DEAL_RANK_DISCOUNT_ENABLE("Ранговая скидка для всех", "deal.rank.discount.enable"),
    DEAL_BTC_MAX_ENTERED_SUM("Максимальный введенный BTC", "deal.btc.max.entered.sum"),
    DEAL_LTC_MAX_ENTERED_SUM("Максимальный введенный LTC", "deal.ltc.max.entered.sum"),
    DEAL_USDT_MAX_ENTERED_SUM("Максимальный введенный USDT", "deal.usdt.max.entered.sum"),
    DEAL_XMR_MAX_ENTERED_SUM("Максимальный введенный XMR", "deal.xmr.max.entered.sum"),
    FIX_COMMISSION_VIP("Фикс комиссия для вип", "commission.fix.vip"),
    MAX_SUM("Максимальная сумма обмена", "max.sum"),
    PAYSCROW_BOUND("Максимальная сумма для Payscrow", "payscrow.bound.amount"),
    DASH_PAY_BOUND("Максимальная сумма для DashPay", "dashpay.bound.amount"),
    ALFA_TEAM_BOUND("Максимальная сумма для AlfaTeam", "alfateam.bound.amount"),
    ALFA_TEAM_TJS_BOUND("Максимальная сумма для AlfaTeam TJS", "alfateam.tjs.bound.amount"),
    ALFA_TEAM_VTB_BOUND("Максимальная сумма для AlfaTeam VTB", "alfateam.vtb.bound.amount"),
    ALFA_TEAM_ALFA_BOUND("Максимальная сумма для AlfaTeam ALFA", "alfateam.alfa.bound.amount"),
    ALFA_TEAM_SBER_BOUND("Максимальная сумма для AlfaTeam SBER", "alfateam.sber.bound.amount"),
    PAY_POINTS_BOUND("Максимальная сумма для Paypoints", "paypoints.bound.amount"),
    ONLY_PAYS_BOUND("Максимальная сумма для OnlyPays", "onlypays.bound.amount"),
    EVO_PAY_BOUND("Максимальная сумма для EvoPay", "evopay.bound.amount"),
    NICE_PAY_BOUND("Максимальная сумма для NicePay", "nicepay.bound.amount"),
    MERCHANT_LIST("Список мерчантов", "merchant.list"),
    REVIEW_PUBLISH_MINUTES_INTERVAL("Промежуток в минутах публикации отзывов", "review.publish.minutes.interval"),
    NUMBER_OF_MERCHANT_ATTEMPTS("Количество попыток получения реквизитов мерчанта", "merchant.attempts.number"),
    DELAY_MERCHANT_ATTEMPTS("Пауза между попытками", "merchant.attempts.delay");

    final String displayName;
    final String key;

    VariableType(String displayName, String key) {
        this.displayName = displayName;
        this.key = key;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getKey() {
        return key;
    }

    public String getKey(CryptoCurrency cryptoCurrency) {
        return key.concat("." + cryptoCurrency.getShortName());
    }

    public String getKey(DealType dealType, CryptoCurrency cryptoCurrency) {
        return key.concat("." + dealType.getKey()).concat("." + cryptoCurrency.getShortName());
    }
}