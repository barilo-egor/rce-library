package tgb.btc.library.constants.enums.bot;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BalanceAuditType {
    MANUAL("Ручное новое значение."),
    MANUAL_ADDITION("Ручное начисление."),
    MANUAL_DEBITING("Ручное списание"),
    REFERRAL_ADDITION("Начисление от рефералов."),
    REFERRAL_DEBITING("Списание для скидки."),
    REVIEW("Отзыв.");

    final String description;

}
