package tgb.btc.library.interfaces.service.design;

import tgb.btc.library.interfaces.enums.MessageImage;

public interface IMessageImageService {

    String getFileId(MessageImage messageImage);

    String getMessage(MessageImage messageImage);

    Integer getSubType(MessageImage messageImage);

    String getFormat(MessageImage messageImage);
}
