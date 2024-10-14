package tgb.btc.library.vo.web.electrum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SingleTransactionElectrumResponse {

    private Integer id;

    private String jsonrpc;

    private String result;

    private ElectrumError error;
}
