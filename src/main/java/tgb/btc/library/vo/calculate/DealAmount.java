package tgb.btc.library.vo.calculate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.bot.DealType;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DealAmount {

    private CalculateData calculateData;

    private BigDecimal amount;

    private BigDecimal cryptoAmount;

    private BigDecimal commission;

    private boolean isEnteredInCrypto;

    private DealType dealType;

    private Long chatId;

    private BigDecimal creditedAmount;

    private BigDecimal cryptoCourse;

    public BigDecimal getCryptoCourse() {
        return cryptoCourse;
    }

    public void setCryptoCourse(BigDecimal cryptoCourse) {
        this.cryptoCourse = cryptoCourse;
    }

    public DealType getDealType() {
        return dealType;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public boolean isEnteredInCrypto() {
        return isEnteredInCrypto;
    }

    public void setEnteredInCrypto(boolean enteredInCrypto) {
        isEnteredInCrypto = enteredInCrypto;
    }

    public CalculateData getCalculateData() {
        return calculateData;
    }

    public void setCalculateData(CalculateData calculateData) {
        this.calculateData = calculateData;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCryptoAmount() {
        return cryptoAmount;
    }

    public void setCryptoAmount(BigDecimal cryptoAmount) {
        this.cryptoAmount = cryptoAmount;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public void setDealType(DealType dealType) {
        this.dealType = dealType;
    }

    public BigDecimal getCreditedAmount() {
        return creditedAmount;
    }

    public void setCreditedAmount(BigDecimal creditedAmount) {
        this.creditedAmount = creditedAmount;
    }

    public void updateDeal(Deal deal) {
        deal.setAmount(amount);
        deal.setCryptoAmount(cryptoAmount);
        deal.setCommission(commission);
        deal.setOriginalPrice(amount);
        deal.setCreditedAmount(creditedAmount);
        deal.setCourse(cryptoCourse);
    }

    public BigDecimal getAmountWithoutCommission() {
        if (DealType.BUY.equals(dealType)) return amount.subtract(commission);
        else return amount.add(commission);
    }
}
