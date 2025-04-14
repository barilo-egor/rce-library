package tgb.btc.library.vo.web.merchant.wellbit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import tgb.btc.library.constants.enums.web.merchant.wellbit.WellBitStatus;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {

    private Payment payment;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Payment {
        private Long id;

        private String credential;

        @JsonProperty("credential_additional_bank")
        private String credentialAdditionalBank;

        @JsonDeserialize(using = WellBitStatus.Deserializer.class)
        private WellBitStatus status;
    }
}
