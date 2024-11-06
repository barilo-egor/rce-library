package tgb.btc.library.vo.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PoolDeal {

    private Long id;

    private String bot;

    private Long pid;

    private String address;

    private String amount;

}
