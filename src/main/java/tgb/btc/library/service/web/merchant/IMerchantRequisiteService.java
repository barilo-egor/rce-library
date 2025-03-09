package tgb.btc.library.service.web.merchant;

import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.vo.RequisiteVO;

public interface IMerchantRequisiteService {

    RequisiteVO getRequisite(Deal deal);

    Merchant getMerchant();
}
