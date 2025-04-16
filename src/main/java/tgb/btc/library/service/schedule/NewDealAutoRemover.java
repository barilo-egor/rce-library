package tgb.btc.library.service.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.interfaces.service.bean.bot.deal.IModifyDealService;
import tgb.btc.library.interfaces.service.bean.bot.deal.IReadDealService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class NewDealAutoRemover {

    private final IReadDealService readDealService;

    private final IModifyDealService modifyDealService;

    public NewDealAutoRemover(IReadDealService readDealService, IModifyDealService modifyDealService) {
        this.readDealService = readDealService;
        this.modifyDealService = modifyDealService;
    }

    @Scheduled(cron = "0 10 3 * * *")
    public void autoRemove() {
        List<Deal> deals = readDealService.getNewDealsByDateTimeBefore(LocalDateTime.now().toLocalDate().atStartOfDay());
        if (deals.isEmpty()) {
            log.info("Отсутствуют сделки в статусе NEW, созданные до сегодняшнего дня для автоматического удаления.");
            return;
        }
        List<Long> pids = deals.stream().map(Deal::getPid).toList();
        log.info("Будут удалены сделки в статусе NEW, созданные до сегодняшнего дня. Идентификаторы: {}",
                pids.stream().map(Object::toString).collect(Collectors.joining()));
        modifyDealService.deleteByPidIn(pids);
    }
}
