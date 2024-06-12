package tgb.btc.library.service.schedule;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tgb.btc.api.web.INotifier;
import tgb.btc.library.conditional.BackupCondition;
import tgb.btc.library.constants.enums.properties.PropertiesPath;
import tgb.btc.library.repository.bot.UserRepository;
import tgb.btc.library.service.process.BackupService;

import java.util.ArrayList;
import java.util.List;

@Service
@Conditional(BackupCondition.class)
@Slf4j
public class BackupScheduler {


    private BackupService backupService;

    private INotifier notifier;

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setNotifier(INotifier notifier) {
        this.notifier = notifier;
    }

    @Autowired
    public void setBackupService(BackupService backupService) {
        this.backupService = backupService;
    }

    @Scheduled(cron = "0 0 03 * * *")
    public void runBackup() {
        String strChatIds = PropertiesPath.CONFIG_PROPERTIES.getString("backup.chatIds");
        if (StringUtils.isBlank(strChatIds)) {
            log.info("Не найден ни один chatId для рассылки ежедневного бэкапа.");
            return;
        }
        try {
            List<Long> chatIds = new ArrayList<>();
            for (String strChatId : strChatIds.split(",")) {
                try {
                    chatIds.add(Long.parseLong(strChatId));
                } catch (NumberFormatException e) {
                    log.error("Не получилось спарсить chatId={} для выгрузки бэкапа.", strChatIds);
                }
            }
            backupService.backup(file -> notifier.sendFile(chatIds, file));
        } catch (Exception e) {
            log.error("Ошибка при ежедневной выгрузке бэкапа.", e);
        }
    }

}
