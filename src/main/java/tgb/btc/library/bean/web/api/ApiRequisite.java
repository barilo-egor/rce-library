package tgb.btc.library.bean.web.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import tgb.btc.library.bean.BasePersist;


@Entity
@Table(name = "API_REQUISITE")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class ApiRequisite extends BasePersist {

    @ManyToOne
    @JsonIgnore
    private ApiPaymentType apiPaymentType;

    private String requisite;

    private String comment;

    @Column(nullable = false)
    private Boolean isOn;
}
