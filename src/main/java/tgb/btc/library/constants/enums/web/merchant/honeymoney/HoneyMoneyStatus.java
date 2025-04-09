package tgb.btc.library.constants.enums.web.merchant.honeymoney;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HoneyMoneyStatus {
    PENDING("В процессе"),
    DENIED("Отклонено"),
    SUCCESSFUL("Успешно");

    private final String description;
}
