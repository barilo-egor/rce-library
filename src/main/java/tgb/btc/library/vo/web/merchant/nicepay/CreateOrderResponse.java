package tgb.btc.library.vo.web.merchant.nicepay;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import tgb.btc.library.constants.enums.web.merchant.nicepay.NicePayStatus;

import java.io.IOException;

@Data
public class CreateOrderResponse {

    @JsonDeserialize(using = Status.Deserializer.class)
    private Status status;

    private Data data;

    @lombok.Data
    public static class Data {

        private String paymentId;

        private SubMethod subMethod;

        @JsonDeserialize(using = NicePayStatus.Deserializer.class)
        private NicePayStatus status;

        private Details details;

        @lombok.Data
        public static class Details {

            @JsonDeserialize(using = Type.Deserializer.class)
            private Type type;

            private String wallet;

            @AllArgsConstructor
            @Getter
            public enum Type {
                PHONE("phone"),
                CARD("card");

                private final String value;

                public static Type fromValue(String value) {
                    for (Type type : Type.values()) {
                        if (type.getValue().equals(value)) {
                            return type;
                        }
                    }
                    return null;
                }

                public static class Deserializer extends JsonDeserializer<Type> {

                    @Override
                    public Type deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
                        return Type.fromValue(p.getValueAsString());
                    }
                }
            }
        }

        @lombok.Data
        public static class SubMethod {

            private Names names;

            @lombok.Data
            public static class Names {

                private String ru;
            }
        }
    }

    @AllArgsConstructor
    @Getter
    public enum Status {
        DETAILS_NOT_FOUND("details_not_found"),
        DETAILS_FOUND("details_found"),
        ERROR("error");

        private final String value;

        public static Status fromValue(String value) {
            for (Status status : Status.values()) {
                if (status.getValue().equals(value)) {
                    return status;
                }
            }
            return null;
        }

        public static class Deserializer extends JsonDeserializer<Status> {

            @Override
            public Status deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
                return Status.fromValue(p.getValueAsString());
            }
        }
    }
}
