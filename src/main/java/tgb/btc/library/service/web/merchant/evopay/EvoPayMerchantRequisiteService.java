package tgb.btc.library.service.web.merchant.evopay;

import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.service.web.merchant.IMerchantRequisiteService;
import tgb.btc.library.vo.RequisiteVO;

@Service
public class EvoPayMerchantRequisiteService implements IMerchantRequisiteService {

    private

    @Override
    public RequisiteVO getRequisite(Deal deal) {
        return null;
    }

    @Override
    public Merchant getMerchant() {
        return Merchant.EVO_PAY;
    }
}
