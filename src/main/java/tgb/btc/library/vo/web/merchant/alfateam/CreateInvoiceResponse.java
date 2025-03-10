package tgb.btc.library.vo.web.merchant.alfateam;

import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class CreateInvoiceResponse {

    private String id;

    private List<DealDTO> deals;

    public boolean hasRequisites() {
        return Objects.nonNull(deals) && !deals.isEmpty();
    }
}
