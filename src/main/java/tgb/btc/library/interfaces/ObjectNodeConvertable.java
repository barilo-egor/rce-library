package tgb.btc.library.interfaces;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.function.Function;

public interface ObjectNodeConvertable<T> {

    Function<T, ObjectNode> mapFunction();
}
