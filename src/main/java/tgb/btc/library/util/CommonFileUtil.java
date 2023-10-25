package tgb.btc.library.util;

import tgb.btc.library.bean.bot.PaymentReceipt;
import tgb.btc.library.constants.strings.FilePaths;

public final class CommonFileUtil {

    private CommonFileUtil() {
    }

    public static String getReceipt(PaymentReceipt paymentReceipt, Long dealPid) {
        return FilePaths.DEALS_RECEIPTS + "/" +
                dealPid + "." + paymentReceipt.getReceiptFormat().name().toLowerCase();
    }
}
