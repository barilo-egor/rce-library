package tgb.btc.library.interfaces.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.strings.CommonMessage;

@Getter
@AllArgsConstructor
public enum MessageImage {
    START("Стартовое сообщение.", """
            ✅БОТ АВТО-ОБМЕНА
            
            —Моментальный перевод на ваш кошелёк.
            —Минимальные комиссии.
            —Максимально быстрые зачисления.\s
            —Не принимаем оплаты от третьих лиц и мошенников
            —Не принимаем грязную и санкционную криптовалюту
            —Выплачиваем чистые рубли"""),
    MAIN_MENU("Сообщение, отправляющее меню.", """
            ✅Нажми "Купить" или "Продать" и выбери криптовалюту для расчёта.️
            
            \uD83C\uDFB0Чтобы получить возможность играть в "Лотерею", необходимо совершить сделку."""),
    DRAWS("Сообщение, отправляющее меню розыгрышей.", "Розыгрыши от обменника\uD83E\uDD73"),
    ROULETTE("Сообщение рулетки.", "Рулетка."),
    CHOOSE_FIAT("Выбор фиата.", "Выберите валюту."),
    CHOOSE_CRYPTO_CURRENCY_BUY("Выбор криптовалюты для покупки.", "Выберите криптовалюту для покупки."),
    CHOOSE_CRYPTO_CURRENCY_SELL("Выбор криптовалюты для продажи.", "Выберите криптовалюту для продажи."),
    PAYMENT_TYPES_BUY("Выбор типа оплаты покупки.", "Выберите способ оплаты:"),
    PAYMENT_TYPES_SELL("Выбор типа оплаты продажи.", "Выберите способ получения перевода:"),
    REFERRAL("Реферальная программа.", "Реферальная программа."),
    CONTACTS("Контакты.", "Контакты."),
    BITCOIN_INPUT_WALLET("Ввод кошелька BTC.", CommonMessage.INPUT_WALLET),
    LITECOIN_INPUT_WALLET("Ввод кошелька LTC.", CommonMessage.INPUT_WALLET),
    USDT_INPUT_WALLET("Ввод кошелька USDT.", CommonMessage.INPUT_WALLET),
    MONERO_INPUT_WALLET("Ввод кошелька XMR.", CommonMessage.INPUT_WALLET),
    SAVED_WALLET("Предложение использовать сохраненный кошелек.", "Вы можете использовать ваш сохраненный адрес: %"),
    FIAT_INPUT_DETAILS("Ввод реквизитов для продажи.", "Введите %s реквизиты, куда вы хотите получить %s%s."),
    MAX_AMOUNT("Сообщение, в случае обмена больше допустимой суммы.", "Вы выбрали большую сумму для обмена, для обмена такой суммы свяжитесь с оператором."),
    WON_LOTTERY("Сообщение при выигрыше лотереи.", "Ты победил!"),
    LOSE_LOTTERY("Сообщение при проигрыше лотереи.", "К сожалению, ты проиграл."),
    BOT_OFF("Сообщение при выключенном боте.", "Приносим свои извинения, проводятся технические работы."),
    DEAL_ADDED_TO_POOL("Сообщение пользователю после добавления сделки в пул.", """
            Валюта отправлена✅
            Ожидайте выхода транзакции в сеть блокчейн:
            %s"""),
    DEAL_CONFIRMED_BITCOIN("Подтверждение сделки BTC / вывод пула.", """
            Валюта вышла в сеть✅
            Ваш хеш транзакции:
            %s"""),
    DEAL_CONFIRMED_LITECOIN("Подтверждение сделки LTC.",  CommonMessage.DEAL_CONFIRMED),
    DEAL_CONFIRMED_USDT("Подтверждение сделки USDT.", CommonMessage.DEAL_CONFIRMED),
    DEAL_CONFIRMED_MONERO("Подтверждение сделки XMR.", CommonMessage.DEAL_CONFIRMED),
    INPUT_DEAL_SUM("Запрос ввода суммы(не BTC)", "✅ Введите нужную сумму в %s.\n\uD83E\uDD16Оплата будет проверена автоматически."),
    INPUT_BITCOIN_DEAL_SUM("Запрос ввода суммы(для BTC)", "✅ Введите нужную сумму в %s или рублях.\n🤖Оплата будет проверена автоматически."),
    BUILD_DEAL_BUY("Формирование сделки покупки.", """
            ✅<b>Заявка №</b><code>%S</code>
            
            <b>Получаете</b>: %s %s
            <b>%s-адрес</b>:<code>%s</code>
          
            Ваш ранг: %s, скидка %s %
            
            <b>\uD83D\uDCB5Сумма к оплате</b>: <code>%s %s</code>
            <b>Реквизиты для оплаты:</b>
            
            <code>%s</code>
            
            <b>⏳Заявка действительна</b>: %s минут
            %s
            ☑️После успешного перевода денег по указанным реквизитам нажмите на кнопку <b>%s</b>
            или же вы можете отменить данную заявку, нажав на кнопку <b>"Отменить заявку</b>.
            %s"""),
    BUILD_DEAL_SELL("Формирование сделки продажи.", """
            ✅<b>Заявка №</b><code>%s</code>
            
            <b>Продаете</b>: %s %s
            <b>%s реквизиты</b>: <code>%s</code>
            
            Ваш ранг: %s, скидка %s %
            
            \\uD83D\\uDCB5<b>Получаете</b>: <code>%s %s</code>
            <b>Реквизиты для перевода %s:</b>
            
            <code>%s</code>
            
            ⏳<b>Заявка действительна</b>: %s минут
            %s
            ☑️После успешного перевода денег по указанному кошельку нажмите на кнопку <b>%s</b>
            или же вы можете отменить данную заявку, нажав на кнопку <b>Отменить заявку</b>.
            %s
            """
            )
    ;

    final String description;
    final String defaultMessage;

    public static MessageImage getInputWallet(CryptoCurrency cryptoCurrency) {
        return switch (cryptoCurrency) {
            case LITECOIN -> LITECOIN_INPUT_WALLET;
            case USDT -> USDT_INPUT_WALLET;
            case MONERO -> MONERO_INPUT_WALLET;
            case BITCOIN -> BITCOIN_INPUT_WALLET;
        };
    }
}
