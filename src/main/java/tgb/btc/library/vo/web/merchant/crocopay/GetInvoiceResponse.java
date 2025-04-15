package tgb.btc.library.vo.web.merchant.crocopay;

import lombok.Data;

@Data
public class GetInvoiceResponse {

    private String message;

    private Transaction transaction;
}
