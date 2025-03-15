package tgb.btc.library.vo.web.merchant.dashpay;

import lombok.Data;

@Data
public class DashPayOrderStatusResponse {

    private Data data;

    @lombok.Data
    public static class Data {

        private Order order;

        @lombok.Data
        public static class Order {

            private Boolean checking;

            private Boolean payed;

            private String redirectUrl;

            @lombok.Data
            public static class Failed {

                private Boolean isCanceled;

                private Boolean isExpired;

                private Boolean emptyTrader;

                private String redirectUrl;
            }
        }
    }
}
