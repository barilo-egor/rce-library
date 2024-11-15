package tgb.btc.library.service.enums;

import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.bot.DeliveryType;
import tgb.btc.library.interfaces.enums.IDeliveryTypeService;
import tgb.btc.library.service.properties.DesignPropertiesReader;
import tgb.btc.library.vo.enums.DeliveryTypeVO;

import java.util.EnumMap;
import java.util.Map;

@Service
public class DeliveryTypeService implements IDeliveryTypeService {

    private DesignPropertiesReader designPropertiesReader;

    @Autowired
    public void setDesignPropertiesReader(DesignPropertiesReader designPropertiesReader) {
        this.designPropertiesReader = designPropertiesReader;
    }

    private Map<DeliveryType, String> displayNames;

    @PostConstruct
    private void initDisplayNames() {
        displayNames = new EnumMap<>(DeliveryType.class);
        for (DeliveryType deliveryType : DeliveryType.values()) {
            displayNames.put(deliveryType, designPropertiesReader.getString(deliveryType.name()));
        }
    }

    @Override
    public String getDisplayName(DeliveryType deliveryType) {
        return StringUtils.defaultString(displayNames.get(deliveryType), deliveryType.name());
    }

    @Override
    public DeliveryTypeVO getVO(DeliveryType deliveryType) {
        return new DeliveryTypeVO(deliveryType.name(), getDisplayName(deliveryType));
    }
}
