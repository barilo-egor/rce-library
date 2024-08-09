package tgb.btc.library.bean.web.api;

import com.sun.istack.NotNull;
import lombok.*;
import tgb.btc.library.bean.BasePersist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
    private ApiPaymentType apiPaymentType;

    private String requisite;

    @NotNull
    @Column(nullable = false)
    private Boolean isOn;
}
