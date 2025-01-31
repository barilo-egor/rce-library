package tgb.btc.library.vo.web.electrum;

import lombok.Builder;
import lombok.Data;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;

@Data
@Builder
public class WithdrawalRequest {

    private CryptoCurrency cryptoCurrency;

    private String amount;

    private String address;

    private String fee;
}
