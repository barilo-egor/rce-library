package tgb.btc.library.bean.bot;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.constants.enums.CreateType;
import tgb.btc.library.constants.enums.bot.*;
import tgb.btc.library.constants.enums.web.merchant.payscrow.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "DEAL")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Deal extends BasePersist {
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Column(name = "DATE_TIME")
    private LocalDateTime dateTime;

    @ManyToOne
    private PaymentType paymentType;

    @Column(name = "CRYPTO_AMOUNT", precision = 15, scale = 8)
    private BigDecimal cryptoAmount;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "WALLET")
    private String wallet;

    @Column(name = "VERIFICATION_PHOTO")
    private String verificationPhoto;

    @Column(name = "IS_USED_PROMO")
    private Boolean isUsedPromo;

    @Column(name = "IS_USED_REFERRAL_DISCOUNT")
    private Boolean isUsedReferralDiscount;

    @Column(name = "CRYPTO_CURRENCY")
    @Enumerated(value = EnumType.STRING)
    private CryptoCurrency cryptoCurrency;

    @Column(name = "DEAL_TYPE")
    @Enumerated(value = EnumType.STRING)
    private DealType dealType;

    @Column(name = "COMMISSION")
    private BigDecimal commission;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<PaymentReceipt> paymentReceipts;

    @Column(name = "DISCOUNT")
    private BigDecimal discount;

    @Column(name = "ORIGINAL_PRICE")
    private BigDecimal originalPrice;

    @Column(name = "IS_PERSONAL_APPLIED")
    private Boolean isPersonalApplied;

    @Column(name = "FIAT_CURRENCY")
    @Enumerated(value = EnumType.STRING)
    private FiatCurrency fiatCurrency;

    @Column(name = "DEAL_STATUS")
    @Enumerated(value = EnumType.STRING)
    private DealStatus dealStatus;

    @Column(name = "ADDITIONAL_VERIFICATION_IMAGE_ID")
    private String additionalVerificationImageId;

    @Column(name = "DELIVERY_TYPE")
    @Enumerated(value = EnumType.STRING)
    private DeliveryType deliveryType;

    @Column(name = "CREDITED_AMOUNT")
    private BigDecimal creditedAmount;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "CREATE_TYPE", columnDefinition = "varchar(20) default 'BOT'")
    private CreateType createType;

    @Column(name = "COURSE")
    private BigDecimal course;

    /**
     * Реквизит на который должен отправить сумму клиент
     */
    @Column(name = "DETAILS")
    private String details;

    @Column(name = "HASH")
    private String hash;

    private Integer payscrowOrderId;

    private OrderStatus payscrowOrderStatus;

    // TODO заменить на сеттеры геттеры ломбока
    public Boolean getUsedPromo() {
        return isUsedPromo;
    }

    public void setUsedPromo(Boolean usedPromo) {
        isUsedPromo = usedPromo;
    }

    public Boolean getUsedReferralDiscount() {
        return isUsedReferralDiscount;
    }

    public Boolean getPersonalApplied() {
        return isPersonalApplied;
    }

    public void setPersonalApplied(Boolean personalApplied) {
        isPersonalApplied = personalApplied;
    }

    public String manualToString() {
        return "Deal{" +
                ", cryptoAmount=" + cryptoAmount +
                ", amount=" + amount +
                ", cryptoCurrency=" + (Objects.nonNull(cryptoCurrency) ? cryptoCurrency.name() : "null") +
                ", dealType=" + (Objects.nonNull(dealType) ? dealType.name() : "null") +
                ", fiatCurrency=" + (Objects.nonNull(fiatCurrency) ? fiatCurrency.name() : "null") +
                ", dealStatus=" + (Objects.nonNull(dealStatus) ? dealStatus.name() : "null") +
                '}';
    }



}