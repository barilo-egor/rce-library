package tgb.btc.library.bean.bot;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.interfaces.JsonConvertable;
import tgb.btc.library.util.web.JacksonUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name = "DEAL_PAYMENT")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DealPayment extends BasePersist implements JsonConvertable {

    @Column(name = "TITLE")
    private String title;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "APP")
    private String app;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "DATE_TIME")
    private LocalDateTime dateTime;

    @OneToOne
    private Deal deal;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Deal getDeal() {
        return deal;
    }

    public void setDeal(Deal deal) {
        this.deal = deal;
    }

    @Override
    public ObjectNode map() {
        return JacksonUtil.getEmpty()
                .put("deal.pid", Objects.nonNull(deal) ? deal.getPid().toString() : "Не привязан")
                .put("app", app)
                .put("title", title)
                .put("message", message)
                .put("phone", phone)
                .put("dateTime", dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
    }

}
