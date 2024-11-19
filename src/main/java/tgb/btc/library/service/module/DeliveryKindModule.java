package tgb.btc.library.service.module;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.DeliveryKind;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.IModule;
import tgb.btc.library.service.properties.ModulesPropertiesReader;

import java.util.Objects;

@Service
@Slf4j
public class DeliveryKindModule implements IModule<DeliveryKind> {

    private DeliveryKind current;

    private ModulesPropertiesReader modulesPropertiesReader;

    @Autowired
    public void setModulesPropertiesReader(ModulesPropertiesReader modulesPropertiesReader) {
        this.modulesPropertiesReader = modulesPropertiesReader;
    }

    @Override
    public DeliveryKind getCurrent() {
        if (Objects.nonNull(current))
            return current;
        String type = modulesPropertiesReader.getString("delivery.kind", DeliveryKind.NONE.name());
        try {
            DeliveryKind deliveryKind = DeliveryKind.valueOf(type);
            current = deliveryKind;
            return deliveryKind;
        } catch (IllegalArgumentException e) {
            String message = "В проперти delivery.kind из modules.properties установлено невалидное значение.";
            log.error(message);
            throw new BaseException(message, e);
        }
    }

    @Override
    public void set(DeliveryKind deliveryKind) {
        modulesPropertiesReader.setProperty("delivery.kind", deliveryKind.name());
        current = deliveryKind;
    }

}
