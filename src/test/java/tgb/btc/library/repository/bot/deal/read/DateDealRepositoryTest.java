package tgb.btc.library.repository.bot.deal.read;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.repository.bot.DealRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
class DateDealRepositoryTest {

    @Autowired
    private DealRepository dealRepository;

    @Test
    @DisplayName("Должен вернуть по диапазону дат внутри.")
    void getByDateBetween() {
        for (int i = 1; i <= 10; i++) {
            dealRepository.save(Deal.builder()
                    .dateTime(LocalDateTime.of(2000, 1, i, 0, 0))
                    .dealStatus(DealStatus.CONFIRMED)
                    .build()
            );
        }
        List<Deal> actualDeals = dealRepository.getConfirmedByDateTimeBetween(
                LocalDateTime.of(2000, 1, 3, 0, 0),
                LocalDateTime.of(2000, 1, 7, 0, 0)
        );
        assertAll(
                () -> assertEquals(5, actualDeals.size()),
                () -> assertEquals(2000, actualDeals.get(0).getDateTime().getYear()),
                () -> assertEquals(1, actualDeals.get(0).getDateTime().getMonth().getValue()),
                () -> assertEquals(3, actualDeals.get(0).getDateTime().getDayOfMonth()),
                () -> assertEquals(4, actualDeals.get(1).getDateTime().getDayOfMonth()),
                () -> assertEquals(5, actualDeals.get(2).getDateTime().getDayOfMonth()),
                () -> assertEquals(6, actualDeals.get(3).getDateTime().getDayOfMonth()),
                () -> assertEquals(7, actualDeals.get(4).getDateTime().getDayOfMonth())
        );
    }

    @Test
    @DisplayName("Должен вернуть по диапазону дат с пустыми датами за нижней границей.")
    void getByDateBetweenBottomBound() {
        for (int i = 1; i <= 10; i++) {
            dealRepository.save(Deal.builder()
                    .dateTime(LocalDateTime.of(2000, 1, i, 0, 0))
                    .dealStatus(DealStatus.CONFIRMED)
                    .build()
            );
        }
        List<Deal> actualDeals = dealRepository.getConfirmedByDateTimeBetween(
                LocalDateTime.of(1999, 5, 29, 0, 0),
                LocalDateTime.of(2000, 1, 4, 0, 0)
        );
        assertAll(
                () -> assertEquals(4, actualDeals.size()),
                () -> assertEquals(2000, actualDeals.get(0).getDateTime().getYear()),
                () -> assertEquals(1, actualDeals.get(0).getDateTime().getMonth().getValue()),
                () -> assertEquals(1, actualDeals.get(0).getDateTime().getDayOfMonth()),
                () -> assertEquals(2, actualDeals.get(1).getDateTime().getDayOfMonth()),
                () -> assertEquals(3, actualDeals.get(2).getDateTime().getDayOfMonth()),
                () -> assertEquals(4, actualDeals.get(3).getDateTime().getDayOfMonth())
        );
    }

    @Test
    @DisplayName("Должен вернуть по диапазону дат с пустыми датами за верхней границей.")
    void getByDateBetweenTopBound() {
        for (int i = 1; i <= 10; i++) {
            dealRepository.save(Deal.builder()
                    .dateTime(LocalDateTime.of(2000, 1, i, 0, 0))
                    .dealStatus(DealStatus.CONFIRMED)
                    .build()
            );
        }
        List<Deal> actualDeals = dealRepository.getConfirmedByDateTimeBetween(
                LocalDateTime.of(2000, 1, 7, 0, 0),
                LocalDateTime.of(2020, 1, 1, 0, 0)
        );
        assertAll(
                () -> assertEquals(4, actualDeals.size()),
                () -> assertEquals(2000, actualDeals.get(0).getDateTime().getYear()),
                () -> assertEquals(1, actualDeals.get(0).getDateTime().getMonth().getValue()),
                () -> assertEquals(7, actualDeals.get(0).getDateTime().getDayOfMonth()),
                () -> assertEquals(8, actualDeals.get(1).getDateTime().getDayOfMonth()),
                () -> assertEquals(9, actualDeals.get(2).getDateTime().getDayOfMonth()),
                () -> assertEquals(10, actualDeals.get(3).getDateTime().getDayOfMonth())
        );
    }

    @Test
    @DisplayName("Должен вернуть по диапазону одной даты.")
    void getByDateBetweenOneDay() {
        for (int i = 1; i <= 10; i++) {
            dealRepository.save(Deal.builder()
                    .dateTime(LocalDateTime.of(2000, 1, 7, 0, i))
                    .dealStatus(DealStatus.CONFIRMED)
                    .build()
            );
        }
        List<Deal> actualDeals = dealRepository.getConfirmedByDateTimeBetween(
                LocalDateTime.of(2000, 1, 7, 0, 0),
                LocalDateTime.of(2000, 1, 7, 23, 59)
        );
        assertAll(
                () -> assertEquals(10, actualDeals.size()),
                () -> assertEquals(2000, actualDeals.get(0).getDateTime().getYear()),
                () -> assertEquals(1, actualDeals.get(0).getDateTime().getMonth().getValue()),
                () -> assertEquals(7, actualDeals.get(0).getDateTime().getDayOfMonth())
        );
    }

    @Test
    @DisplayName("Должен вернуть пустой лист по диапазону в котором нет сделок.")
    void getByDateBetweenWithoutDeals() {
        for (int i = 1; i <= 10; i++) {
            dealRepository.save(Deal.builder()
                    .dateTime(LocalDateTime.of(2000, 1, i, 0, 0))
                    .dealStatus(DealStatus.CONFIRMED)
                    .build()
            );
        }
        List<Deal> actualDeals = dealRepository.getConfirmedByDateTimeBetween(
                LocalDateTime.of(2015, 1, 7, 0, 0),
                LocalDateTime.of(2020, 1, 1, 0, 0)
        );
        assertAll(
                () -> assertEquals(0, actualDeals.size())
        );
    }

    @Test
    @DisplayName("Должен вернуть пустой лист при отсутствии сделок")
    void getByDateBetweenNoDeals() {
        List<Deal> actualDeals = dealRepository.getConfirmedByDateTimeBetween(
                LocalDateTime.of(1980, 1, 1, 0, 0),
                LocalDateTime.of(2020, 1, 1, 0, 0)
        );
        assertAll(
                () -> assertEquals(0, actualDeals.size())
        );
    }


}
