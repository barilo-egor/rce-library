package tgb.btc.library.vo.enums;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import tgb.btc.library.interfaces.JsonConvertable;
import tgb.btc.library.util.web.JacksonUtil;

@AllArgsConstructor
@Getter
@Setter
public class DeliveryTypeVO implements JsonConvertable {

    private String name;

    private String displayName;

    @Override
    public ObjectNode map() {
        ObjectNode result = JacksonUtil.getEmpty();
        result.put("name", this.getName());
        result.put("displayName", this.getName());
        return result;
    }

}
