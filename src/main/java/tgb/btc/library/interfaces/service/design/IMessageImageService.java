package tgb.btc.library.interfaces.service.design;

import org.springframework.web.multipart.MultipartFile;
import tgb.btc.library.interfaces.enums.MessageImage;

import java.io.File;

public interface IMessageImageService {

    String getFileId(MessageImage messageImage);

    String getMessage(MessageImage messageImage);

    Integer getSubType(MessageImage messageImage);

    String getFormat(MessageImage messageImage);

    String getFormatNullable(MessageImage messageImage);

    File getFile(MessageImage messageImage);

    void updateText(MessageImage messageImage, String text);

    void setImage(MessageImage messageImage, MultipartFile multipartFile);

    void deleteImage(MessageImage messageImage);
}
