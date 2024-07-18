package tgb.btc.library.service.enums;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.CabinetButton;
import tgb.btc.library.interfaces.enums.ICabinetButtonService;
import tgb.btc.library.service.properties.ButtonsDesignPropertiesReader;

@Service
public class CabinetButtonService implements ICabinetButtonService {

    private ButtonsDesignPropertiesReader buttonsDesignPropertiesReader;

    @Autowired
    public void setButtonsDesignPropertiesReader(
            ButtonsDesignPropertiesReader buttonsDesignPropertiesReader) {
        this.buttonsDesignPropertiesReader = buttonsDesignPropertiesReader;
    }

    @Override
    public String getText(CabinetButton cabinetButton) {
        return buttonsDesignPropertiesReader.getString(cabinetButton.name());
    }
}
