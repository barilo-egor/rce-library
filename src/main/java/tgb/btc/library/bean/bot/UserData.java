package tgb.btc.library.bean.bot;

import lombok.*;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "USER_DATA")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class UserData extends BasePersist {

    @OneToOne
    private User user;

    @Column(name = "LONG_VARIABLE")
    private Long longVariable;

    @Column(name = "STRING_VARIABLE")
    private String stringVariable;

    @Column(name = "DEAL_TYPE_VARIABLE")
    private DealType dealTypeVariable;

    @Column(name = "CRYPTO_CURRENCY")
    private CryptoCurrency cryptoCurrency;

    @Column(name = "FIAT_CURRENCY")
    private FiatCurrency fiatCurrency;

}
