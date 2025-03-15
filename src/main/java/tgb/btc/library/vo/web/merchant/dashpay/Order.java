package tgb.btc.library.vo.web.merchant.dashpay;

import lombok.Data;

@Data
public class Order {

    private String id;

    private String externalId;

    private TraderRequisites traderRequisites;

    @Data
    public static class TraderRequisites {

        private String title;

        private String value;

        private String bank;

        private String recipient;
    }
}
