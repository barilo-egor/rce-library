package tgb.btc.library.interfaces;

import com.fasterxml.jackson.databind.node.ObjectNode;

@FunctionalInterface
public interface JsonConvertable {

    ObjectNode map();
}
