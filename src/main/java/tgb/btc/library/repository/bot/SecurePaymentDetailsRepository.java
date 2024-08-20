package tgb.btc.library.repository.bot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tgb.btc.library.bean.bot.SecurePaymentDetails;
import tgb.btc.library.repository.BaseRepository;

@Repository
public interface SecurePaymentDetailsRepository extends JpaRepository<SecurePaymentDetails, Long>, BaseRepository<SecurePaymentDetails> {
}
