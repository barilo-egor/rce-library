package tgb.btc.library.vo.web.merchant.payfinity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tgb.btc.library.constants.enums.web.merchant.payfinity.PayFinityStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetTransactionResponse extends Response<GetTransactionResponse.Data> {

    @lombok.Data
    public static class Data {

        private PayFinityStatus status;
    }
}
