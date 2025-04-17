package tgb.btc.library.bean.bot;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tgb.btc.library.bean.BasePersist;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MerchantSuccessStatus extends BasePersist {

    private String status;
}
