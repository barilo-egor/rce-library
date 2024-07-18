package tgb.btc.library.service.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.constants.enums.properties.PropertiesPath;
import tgb.btc.library.exception.PropertyValueNotFoundException;
import tgb.btc.library.interfaces.util.IBulkDiscountService;
import tgb.btc.library.vo.BulkDiscount;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BulkDiscountService implements IBulkDiscountService {

    public final List<BulkDiscount> bulkDiscounts = new ArrayList<>();

    @PostConstruct
    private void init() {
        for (String key : PropertiesPath.BULK_DISCOUNT_PROPERTIES.getKeys()) {
            int sum;
            if (StringUtils.isBlank(key)) {
                throw new PropertyValueNotFoundException("Не указано название для одного из ключей" + key + ".");
            }
            try {
                sum = Integer.parseInt(key.split("\\.")[3]);
            } catch (NumberFormatException e) {
                throw new PropertyValueNotFoundException("Не корректное название для ключа " + key + ".");
            }
            String value = PropertiesPath.BULK_DISCOUNT_PROPERTIES.getString(key);
            if (StringUtils.isBlank(value)) {
                throw new PropertyValueNotFoundException("Не указано значение для ключа " + key + ".");
            }
            double percent;
            try {
                percent = Double.parseDouble(value);
            } catch (NumberFormatException e) {
                throw new PropertyValueNotFoundException("Не корректное значение для ключа " + key + ".");
            }
            bulkDiscounts.add(BulkDiscount.builder()
                    .percent(percent)
                    .sum(sum)
                    .fiatCurrency(FiatCurrency.getByCode(key.split("\\.")[0]))
                    .dealType(DealType.findByKey((key.split("\\.")[1])))
                    .cryptoCurrency(CryptoCurrency.fromShortName(key.split("\\.")[2]))
                    .build());
        }
        bulkDiscounts.sort(Comparator.comparingInt(BulkDiscount::getSum));
        Collections.reverse(bulkDiscounts);
    }

    @Override
    public BigDecimal getPercentBySum(BigDecimal sum, FiatCurrency fiatCurrency, DealType dealType, CryptoCurrency cryptoCurrency) {
        for (BulkDiscount bulkDiscount : bulkDiscounts.stream()
                .filter(bulkDiscount -> bulkDiscount.getFiatCurrency().equals(fiatCurrency)
                        && bulkDiscount.getDealType().equals(dealType)
                        && bulkDiscount.getCryptoCurrency().equals(cryptoCurrency))
                .collect(Collectors.toList())) {
            if (BigDecimal.valueOf(bulkDiscount.getSum()).compareTo(sum) < 1)
                return BigDecimal.valueOf(bulkDiscount.getPercent());
        }
        return BigDecimal.ZERO;
    }

    @Override
    public void clear() {
        bulkDiscounts.clear();
    }

    @Override
    public void add(BulkDiscount bulkDiscount) {
        bulkDiscounts.add(bulkDiscount);
    }

    @Override
    public void reverse() {
        Collections.reverse(bulkDiscounts);
    }
}
