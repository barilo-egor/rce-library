package tgb.btc.library.vo.web.merchant.paypoints;

import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class PayPointsResponse {

    /**
     * Поля заполняемые в случае ошибки
     */
    private String code;

    private String message;

    private List<Error> errors;

    @Data
    public static class Error {

        private List<String> field;
    }

    public boolean isOk() {
        return (Objects.isNull(code) || code.isBlank()) && (Objects.isNull(message) || message.isBlank())
                && (Objects.isNull(errors) || errors.isEmpty());
    }
}
