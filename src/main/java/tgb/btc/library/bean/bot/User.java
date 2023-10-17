package tgb.btc.library.bean.bot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.interfaces.ICommand;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "USER")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BasePersist {

    public static final int DEFAULT_STEP = 0;

    @Column(name = "CHAT_ID", nullable = false, unique = true)
    private Long chatId;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "STEP", columnDefinition = "int default 0")
    private Integer step;

    @Column(name = "COMMAND", columnDefinition = "varchar(255) default 'START'")
    private String command;

    @Column(name = "REGISTRATION_DATE", nullable = false)
    private LocalDateTime registrationDate;

    @Column(name = "IS_ADMIN", nullable = false)
    private Boolean isAdmin;

    @Column(name = "LOTTERY_COUNT")
    private Integer lotteryCount;

    @Column(name = "FROM_CHAT_ID")
    private Long fromChatId;

    @Column(name = "REFERRAL_BALANCE")
    private Integer referralBalance;

    @Column(name = "CHARGES")
    private Integer charges;

    @Column(name = "BUFFER_VARIABLE", length = 3000)
    private String bufferVariable;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;

    @Column(name = "IS_BANNED", nullable = false)
    private Boolean isBanned;

    @Column(name = "CURRENT_DEAL")
    private Long currentDeal;

    @Column(name = "REFERRAL_PERCENT")
    private BigDecimal referralPercent;

    @OneToMany(fetch = FetchType.EAGER)
    private List<ReferralUser> referralUsers;

    public User(Long pid) {
        this.setPid(pid);
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(ICommand command) {
        this.command = command.name();
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime dateTime) {
        this.registrationDate = dateTime;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Integer getLotteryCount() {
        return lotteryCount;
    }

    public void setLotteryCount(Integer lotteryCount) {
        this.lotteryCount = lotteryCount;
    }

    public Long getFromChatId() {
        return fromChatId;
    }

    public void setFromChatId(Long fromChatId) {
        this.fromChatId = fromChatId;
    }

    public Integer getReferralBalance() {
        return referralBalance;
    }

    public void setReferralBalance(Integer referralBalance) {
        this.referralBalance = referralBalance;
    }

    public List<ReferralUser> getReferralUsers() {
        return referralUsers;
    }

    public void setReferralUsers(List<ReferralUser> referralUsers) {
        this.referralUsers = referralUsers;
    }

    public String getBufferVariable() {
        return bufferVariable;
    }

    public void setBufferVariable(String bufferVariable) {
        this.bufferVariable = bufferVariable;
    }


    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getBanned() {
        return isBanned;
    }

    public void setBanned(Boolean banned) {
        isBanned = banned;
    }

    public Long getCurrentDeal() {
        return currentDeal;
    }

    public void setCurrentDeal(Long currentDeal) {
        this.currentDeal = currentDeal;
    }

    public Integer getCharges() {
        return charges;
    }

    public void setCharges(Integer charges) {
        this.charges = charges;
    }

    public BigDecimal getReferralPercent() {
        return referralPercent;
    }

    public void setReferralPercent(BigDecimal referralPercent) {
        this.referralPercent = referralPercent;
    }

    public static boolean isDefault(int step) {
        return step == DEFAULT_STEP;
    }
}
