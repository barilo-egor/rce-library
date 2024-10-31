package tgb.btc.library.vo.web;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestParam {

    private String key;

    private String value;
}
