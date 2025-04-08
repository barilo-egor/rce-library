package tgb.btc.library.vo.web.merchant.payfinity;

import lombok.Data;

@Data
public class Response<T> {

    private Boolean success;

    private Error error;

    private T data;

    @Data
    public static class Error {

        private String message;

        private Integer code;
    }
}
