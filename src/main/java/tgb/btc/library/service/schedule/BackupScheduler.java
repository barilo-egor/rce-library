package tgb.btc.library.service.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tgb.btc.api.web.INotifier;
import tgb.btc.library.conditional.BackupCondition;
import tgb.btc.library.repository.bot.UserRepository;
import tgb.btc.library.service.process.BackupService;

@Service
@Conditional(BackupCondition.class)
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
        backupService.backup(file -> notifier.sendFile(userRepository.getAdminsChatIds(), file));
    }

}
