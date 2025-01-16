package tgb.btc.library.vo.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tgb.btc.library.interfaces.enums.MessageImage;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileIdContainer {
    private MessageImage type;

    private String fileId;

    private String format;
}
