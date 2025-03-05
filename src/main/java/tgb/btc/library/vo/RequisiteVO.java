package tgb.btc.library.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tgb.btc.library.constants.enums.Merchant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequisiteVO {

    private Merchant merchant;

    private String requisite;
}
