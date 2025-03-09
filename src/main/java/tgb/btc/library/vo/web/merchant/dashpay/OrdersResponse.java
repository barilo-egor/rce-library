package tgb.btc.library.vo.web.merchant.dashpay;

import lombok.Data;

import java.util.List;

@Data
public class OrdersResponse {

    private Data data;

    @lombok.Data
    public static class Data {

        private List<Order> orders;

        @lombok.Data
        public static class Order {

            private String id;

            private Status status;

            @lombok.Data
            public static class Status {

                private String code;
            }
        }
    }
}
