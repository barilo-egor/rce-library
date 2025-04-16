package tgb.btc.library.service.web.merchant.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MerchantErrorLoggingService {

    public void log(Long pid, Exception e) {
        log.error("Ошибка получения реквизитов по сделке №{}:", pid, e);
    }
}
