package tgb.btc.library.repository.bot;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tgb.btc.library.bean.bot.PaymentReceipt;
import tgb.btc.library.repository.BaseRepository;

import java.util.List;

@Repository
public interface PaymentReceiptRepository extends BaseRepository<PaymentReceipt> {

    @Query("from PaymentReceipt where deal.pid in (select pid from Deal where user.chatId=:userChatId)")
    List<PaymentReceipt> getByDealsPids(Long userChatId);
}
