package tgb.btc.library.service.module;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.ReferralType;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.IModule;
import tgb.btc.library.service.properties.ModulesPropertiesReader;

import java.util.Objects;

@Service
@Slf4j
public class ReferralModule implements IModule<ReferralType> {

    private ReferralType current;

    private ModulesPropertiesReader modulesPropertiesReader;

    @Autowired
    public void setModulesPropertiesReader(ModulesPropertiesReader modulesPropertiesReader) {
        this.modulesPropertiesReader = modulesPropertiesReader;
    }

    @Override
    public ReferralType getCurrent() {
        if (Objects.nonNull(current))
            return current;
        String type = modulesPropertiesReader.getString("referral.type", ReferralType.NONE.name());
        try {
            ReferralType referralType = ReferralType.valueOf(type);
            current = referralType;
            return referralType;
        } catch (IllegalArgumentException e) {
            String message = "В проперти referral.type из modules.properties установлено невалидное значение.";
            log.error(message);
            throw new BaseException(message, e);
        }
    }

    @Override
    public void set(ReferralType referralType) {
        modulesPropertiesReader.setProperty("referral.type", referralType.name());
    }
}
