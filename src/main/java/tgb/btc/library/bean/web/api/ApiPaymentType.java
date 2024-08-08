package tgb.btc.library.bean.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.*;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.interfaces.JsonConvertable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "API_PAYMENT_TYPE")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class ApiPaymentType extends BasePersist implements JsonConvertable {

    private String id;

    private String name;

    @Override
    public ObjectNode map() {
        return new ObjectMapper()
                .createObjectNode()
                .put("id", id)
                .put("name", name);
    }
}
