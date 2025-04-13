package tgb.btc.library.constants.enums.web.merchant.payscrow;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentMethodType {
    ALPHA_BANK("Альфа-Банк", "4f591bcc-29f8-4598-9828-5b109a25b509"),
    SBERBANK("Сбербанк", "af3daf65-b6b6-450b-b28d-54b97436ef4a"),
    TINKOFF("Тинькофф", "cd797fcb-5b5a-47a3-8b54-35f5f7000344"),
    ANY_RU_BANK("Любой банк РФ", "b11d98ad-1e09-4e63-8c4a-f1d8a4b9ce3f"),
    SBP_TINKOFF("СБП Тинькофф", "33c6fe18-641d-41f5-a3fc-db975c63fe6f"),
    SBP_ALPHA_BANK("СБП Альфа-Банк", "8880d5b5-2c82-4cef-8f94-b7d5d2cbefe1"),
    SBP_SBERBANK("СБП Сбербанк", "a6636989-0bd9-4cf1-8ec3-83c07b08f25f"),
    SBP("СБП", "894387d7-b8b6-4dab-82ee-dd1106f7369e"),
    VTB_CARD("ВТБ-ВТБ Карта", "cae62b5b-4146-4674-acbc-c5f9b67e6aa1"),
    VTB_SBP("ВТБ-ВТБ СБП", "9a92ec2d-92cd-40a8-aeb6-8c75bcd3c0cd"),
    CROSS_BORDER_SBP("Трансграничный СБП", "b46146af-e597-4409-b24c-43a53b16f026");

    final String description;

    final String methodId;

    public static PaymentMethodType fromMethodId(String methodId) {
        for (PaymentMethodType paymentMethodType : PaymentMethodType.values()) {
            if (paymentMethodType.getMethodId().equals(methodId)) {
                return paymentMethodType;
            }
        }
        return null;
    }
}
