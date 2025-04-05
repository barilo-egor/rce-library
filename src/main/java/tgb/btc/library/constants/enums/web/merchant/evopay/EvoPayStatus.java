package tgb.btc.library.constants.enums.web.merchant.evopay;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

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

    public static List<EvoPayStatus> NOT_FINAL_STATUSES = List.of(CREATED, IN_PROCESS);
}
