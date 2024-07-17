package tgb.btc.library.interfaces.enums;

import tgb.btc.library.constants.enums.bot.DeliveryType;
import tgb.btc.library.vo.enums.DeliveryTypeVO;

public interface IDeliveryTypeService {

    String getDisplayName(DeliveryType deliveryType);

    DeliveryTypeVO getVO(DeliveryType deliveryType);

}
