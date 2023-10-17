package tgb.btc.library.util.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import tgb.btc.library.interfaces.ObjectNodeConvertable;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JacksonUtil {
    private JacksonUtil() {
    }

    public static ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper();

    public static <T> ArrayNode toArrayNode(Collection<T> objects, Function<T, ObjectNode> mapper) {
        return getEmptyArray()
                .addAll(objects.stream()
                        .map(mapper)
                        .collect(Collectors.toList()));
    }

    public static <T> ArrayNode toArrayNode(Collection<T> objects, ObjectNodeConvertable<T> mapper) {
        return getEmptyArray()
                .addAll(objects.stream()
                        .map(object -> mapper.mapFunction().apply(object))
                        .collect(Collectors.toList()));
    }

    public static <T extends ObjectNodeConvertable<T>> ArrayNode toArrayNode(Collection<T> objects) {
        return getEmptyArray()
                .addAll(objects.stream()
                        .map(object -> object.mapFunction().apply(object))
                        .collect(Collectors.toList()));
    }

    public static <T extends ObjectNodeConvertable<T>> ObjectNode toObjectNode(T t) {
        return t.mapFunction().apply(t);
    }

    public static <T> ObjectNode toObjectNode(T t, Function<T, ObjectNode> mapper) {
        return mapper.apply(t);
    }

    public static <T> ObjectNode toObjectNode(T t, ObjectNodeConvertable<T> mapper) {
        return mapper.mapFunction().apply(t);
    }

    public static ObjectNode getEmpty() {
        return DEFAULT_OBJECT_MAPPER.createObjectNode();
    }

    public static ArrayNode getEmptyArray() {
        return DEFAULT_OBJECT_MAPPER.createArrayNode();
    }

    public static ObjectNode toObjectNode(String key, String value) {
        return getEmpty().put(key, value);
    }

    public static <T> ObjectNode pagingData(List<T> list, long total, ObjectNodeConvertable<T> mapper) {
        ObjectNode result = getEmpty();
        result.put("success", true);
        result.put("total", total);
        result.set("items", toArrayNode(list, mapper));
        return result;
    }
}
