package tgb.btc.library.vo.web;

import lombok.Data;

import java.util.Objects;

@Data
public class RequestHeader {

    private String name;

    private String value;

    public void clearValue() {
        this.value = null;
    }

    public boolean isEmpty() {
        return Objects.isNull(value);
    }
}
