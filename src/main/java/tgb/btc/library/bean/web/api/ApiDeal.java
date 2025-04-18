package tgb.btc.library.bean.web.api;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.constants.enums.ApiDealType;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.constants.enums.bot.ReceiptFormat;
import tgb.btc.library.constants.enums.web.ApiDealStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "API_DEAL")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class ApiDeal extends BasePersist {

    @ManyToOne(fetch = FetchType.EAGER)
    private ApiUser apiUser;

    private LocalDateTime dateTime;

    private LocalDateTime paidDateTime;

    @Enumerated(EnumType.STRING)
    private DealType dealType;

    @Column(precision = 15, scale = 8)
    private BigDecimal cryptoAmount;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private ApiDealStatus apiDealStatus;

    @Enumerated(EnumType.STRING)
    private CryptoCurrency cryptoCurrency;

    @Column
    private String requisite;

    @Enumerated(EnumType.STRING)
    private FiatCurrency fiatCurrency;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'API'")
    @Builder.Default
    @Column(nullable = false)
    private ApiDealType apiDealType = ApiDealType.API;

    /**
     * ID файла в ТГ
     */
    @Column
    private String checkImageId;

    @Column
    @Enumerated(EnumType.STRING)
    private ReceiptFormat receiptFormat;

    @ManyToOne
    private ApiPaymentType apiPaymentType;

    @ManyToOne
    private ApiRequisite apiRequisite;

    public BigDecimal getAmountToPay() {
        if (DealType.isBuy(dealType)) return amount;
        else return cryptoAmount;
    }
}
