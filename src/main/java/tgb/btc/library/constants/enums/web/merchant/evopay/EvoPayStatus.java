package tgb.btc.library.constants.enums.web.merchant.evopay;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EvoPayStatus {
    CREATED("Создана"),
    IN_PROCESS("В процессе"),
    EXPIRE("Истек"),
    SUCCESS("Успешно"),
    CANCEL("Отменен"),
    APPEAL("В споре");

    final String description;
}
