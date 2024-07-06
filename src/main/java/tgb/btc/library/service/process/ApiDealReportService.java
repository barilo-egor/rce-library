package tgb.btc.library.service.process;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.web.api.ApiDeal;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.util.BigDecimalUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ApiDealReportService {

    public byte[] loadReport(List<ApiDeal> deals) {
        Workbook book = new XSSFWorkbook();
        if (!CollectionUtils.isEmpty(deals)) {
            Sheet sheet = book.createSheet("Сделки");
            fillDealSheet(sheet, deals);
        }
        byte[] result;
        try {
            File currDir = new File(".");
            String path = currDir.getAbsolutePath();
            String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";
            File file = new File(fileLocation);

            FileOutputStream outputStream = new FileOutputStream(fileLocation);
            book.write(outputStream);
            book.close();
            result = Files.readAllBytes(file.toPath());
            if (file.delete()) log.trace("Файл успешно удален.");
            else log.trace("Файл не удален.");
        } catch (IOException t) {
            log.error("Ошибка при выгрузке файла. " + this.getClass().getSimpleName(), t);
            throw new BaseException();
        }
        return result;
    }

    private void fillDealSheet(Sheet apiSheet, List<ApiDeal> apiDeals) {
        Row headRow = apiSheet.createRow(0);
        apiSheet.setDefaultColumnWidth(30);
        Cell headCell = headRow.createCell(0);
        fillApiDealHeadCell(headCell, headRow);
        int i = 2;
        Map<CryptoCurrency, BigDecimal> totalBuyCryptoAmountMap = new HashMap<>();
        Arrays.stream(CryptoCurrency.values())
                .forEach(cryptoCurrency -> totalBuyCryptoAmountMap.put(cryptoCurrency, BigDecimal.ZERO));
        Map<CryptoCurrency, BigDecimal> totalSellCryptoAmountMap = new HashMap<>();
        Arrays.stream(CryptoCurrency.values())
                .forEach(cryptoCurrency -> totalSellCryptoAmountMap.put(cryptoCurrency, BigDecimal.ZERO));

        Map<FiatCurrency, BigDecimal> totalBuyFiatAmountMap = new HashMap<>();
        Arrays.stream(FiatCurrency.values())
                .forEach(fiatCurrency -> totalBuyFiatAmountMap.put(fiatCurrency, BigDecimal.ZERO));
        Map<FiatCurrency, BigDecimal> totalSellFiatAmountMap = new HashMap<>();
        Arrays.stream(FiatCurrency.values())
                .forEach(fiatCurrency -> totalSellFiatAmountMap.put(fiatCurrency, BigDecimal.ZERO));
        for (ApiDeal deal : apiDeals) {
            Row row = apiSheet.createRow(i);
            boolean isBuy = DealType.isBuy(deal.getDealType());
            Cell cell = row.createCell(0);
            cell.setCellValue(deal.getDealType().name());
            cell = row.createCell(1);
            cell.setCellValue(deal.getDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            cell = row.createCell(2);
            cell.setCellValue(deal.getAmount().setScale(0, RoundingMode.FLOOR).toString());
            Map<FiatCurrency, BigDecimal> totalFiatAmountMap = isBuy
                    ? totalBuyFiatAmountMap
                    : totalSellFiatAmountMap;
            totalFiatAmountMap.put(deal.getApiUser().getFiatCurrency(), totalFiatAmountMap.get(deal.getApiUser().getFiatCurrency()).add(deal.getAmount()));
            cell = row.createCell(3);
            cell.setCellValue(deal.getApiUser().getFiatCurrency().getCode());
            cell = row.createCell(4);
            cell.setCellValue(BigDecimalUtil.roundToPlainString(deal.getCryptoAmount(), deal.getCryptoCurrency().getScale()));
            Map<CryptoCurrency, BigDecimal> totalCryptoAmountMap = isBuy
                    ? totalBuyCryptoAmountMap : totalSellCryptoAmountMap;
            totalCryptoAmountMap.put(deal.getCryptoCurrency(), totalCryptoAmountMap.get(deal.getCryptoCurrency()).add(deal.getCryptoAmount()));
            cell = row.createCell(5);
            cell.setCellValue(deal.getCryptoCurrency().getShortName());
            cell = row.createCell(6);
            cell.setCellValue(deal.getApiUser().getId());
            i++;
        }
        i++;

        i += 1;

        FiatCurrency[] fiatCurrencies = FiatCurrency.values();
        CryptoCurrency[] cryptoCurrencies = CryptoCurrency.values();
        int fiatCurrenciesLength = fiatCurrencies.length;
        int cryptoCurrencyLength = cryptoCurrencies.length;
        int maxLength = Math.max(fiatCurrenciesLength, cryptoCurrencyLength);
        Row row = apiSheet.createRow(i);
        Cell cell = row.createCell(2);
        cell.setCellValue("Покупка");
        i = printTotal(apiSheet, i, totalBuyCryptoAmountMap, totalBuyFiatAmountMap, fiatCurrencies, cryptoCurrencies,
                fiatCurrenciesLength, cryptoCurrencyLength, maxLength, 2);
        i++;
        row = apiSheet.createRow(i);
        cell = row.createCell(2);
        cell.setCellValue("Продажа");
        printTotal(apiSheet, i, totalSellCryptoAmountMap, totalSellFiatAmountMap, fiatCurrencies, cryptoCurrencies,
                fiatCurrenciesLength, cryptoCurrencyLength, maxLength, 2);
    }

    private void fillApiDealHeadCell(Cell headCell, Row headRow) {
        headCell.setCellValue("Тип сделки");
        headCell = headRow.createCell(1);
        headCell.setCellValue("Дата, время");
        headCell = headRow.createCell(2);
        headCell.setCellValue("Фиатная сумма");
        headCell = headRow.createCell(3);
        headCell.setCellValue("Фиатная валюта");
        headCell = headRow.createCell(4);
        headCell.setCellValue("Сумма крипты");
        headCell = headRow.createCell(5);
        headCell.setCellValue("Крипто валюта");
        headCell = headRow.createCell(6);
        headCell.setCellValue("ID");
    }

    private int printTotal(Sheet sheet, int i, Map<CryptoCurrency, BigDecimal> totalBuyCryptoAmountMap,
            Map<FiatCurrency, BigDecimal> totalBuyFiatAmountMap, FiatCurrency[] fiatCurrencies,
            CryptoCurrency[] cryptoCurrencies, int fiatCurrenciesLength, int cryptoCurrencyLength,
            int maxLength, int startCell) {
        Row row;
        i++;
        for (int j = 0; j < maxLength; j++) {
            row = sheet.createRow(i);
            if (j < fiatCurrenciesLength) printFiatTotal(row, fiatCurrencies[j], totalBuyFiatAmountMap, startCell);
            if (j < cryptoCurrencyLength)
                printCryptoTotal(row, cryptoCurrencies[j], totalBuyCryptoAmountMap, startCell + 2);
            i++;
        }
        return i;
    }

    private void printFiatTotal(Row row, FiatCurrency fiatCurrency, Map<FiatCurrency, BigDecimal> totalFiatAmountMap, int startCell) {
        Cell cell = row.createCell(startCell);
        cell.setCellValue(BigDecimalUtil.roundToPlainString(totalFiatAmountMap.get(fiatCurrency)));
        cell = row.createCell(startCell + 1);
        cell.setCellValue(fiatCurrency.getGenitive());
    }


    private void printCryptoTotal(Row row, CryptoCurrency cryptoCurrency, Map<CryptoCurrency, BigDecimal> totalFiatAmountMap, int startCell) {
        Cell cell = row.createCell(startCell);
        cell.setCellValue(BigDecimalUtil.roundToPlainString(totalFiatAmountMap.get(cryptoCurrency),
                cryptoCurrency.getScale()));
        cell = row.createCell(startCell + 1);
        cell.setCellValue(cryptoCurrency.getShortName());
    }
}
