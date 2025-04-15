package tgb.btc.library.vo.web.merchant.crocopay;

import lombok.Data;

@Data
public class CreateInvoiceResponse {

    private String message;

    private Response response;

    @Data
    public static class Response {

        private Transaction transaction;

        private PaymentRequisites paymentRequisites;

        @Data
        public static class PaymentRequisites {

            private String paymentMethod;

            private String requisites;
        }
    }
}
