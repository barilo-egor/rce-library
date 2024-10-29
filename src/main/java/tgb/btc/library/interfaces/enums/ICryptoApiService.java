package tgb.btc.library.interfaces.enums;

import tgb.btc.library.constants.enums.CryptoApi;

import java.math.BigDecimal;

public interface ICryptoApiService {

    BigDecimal getCourse(CryptoApi cryptoApi);
}
