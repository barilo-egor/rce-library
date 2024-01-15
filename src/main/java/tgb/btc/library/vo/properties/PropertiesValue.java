package tgb.btc.library.vo.properties;

import com.fasterxml.jackson.databind.node.ObjectNode;
import tgb.btc.library.interfaces.ObjectNodeConvertable;
import tgb.btc.library.util.web.JacksonUtil;

import java.util.function.Function;

public class PropertiesValue implements ObjectNodeConvertable<PropertiesValue> {

    private final String key;

    private final Object value;

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public PropertiesValue(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Function<PropertiesValue, ObjectNode> mapFunction() {
        return apiResponse -> JacksonUtil.getEmpty()
                .put("key", this.key)
                .put("value", String.valueOf(this.value));
    }

    @Override
    public String toString() {
        return "PropertiesValue{" +
                "key='" + key + '\'' +
                ", value=" + value +
                '}';
    }
}
