package tgb.btc.library.bean.web.api;

import lombok.*;
import tgb.btc.library.bean.BasePersist;

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
public class ApiPaymentType extends BasePersist {

    private String id;

    private String name;
}
