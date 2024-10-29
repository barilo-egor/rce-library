package tgb.btc.library.vo.web.electrum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElectrumError {

    private Integer code;

    private String message;
}
