package tgb.btc.library.vo.web.merchant.nicepay;

import lombok.Data;

@Data
public class UploadProofRequest {

    private String merchantId;

    private String secret;

    private String payment;
}
