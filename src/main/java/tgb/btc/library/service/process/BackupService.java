package tgb.btc.library.service.process;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.DriveScopes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tgb.btc.library.conditional.BackupCondition;
import tgb.btc.library.constants.enums.properties.PropertiesPath;
import tgb.btc.library.exception.BackupException;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


@Service
@Slf4j
@Conditional(BackupCondition.class)
public class BackupService {

    @Value("${spring.datasource.username}")
    public String userDB;

    @Value("${spring.datasource.password}")
    public String passwordDB;

    @Async
    public void backup(Consumer<File> consumer) {
        log.info("Запуск процесса резервного копирования");
        final String database = "rce";
        File backupFile = null;
        try {
            backupFile = File.createTempFile("backup-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm_")),
                    ".sql");
            String command = String.format("mysqldump -u %s -p%s %s --result-file=%s",
                    userDB, passwordDB, database, backupFile.getAbsolutePath());
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            consumer.accept(backupFile);
        } catch (Exception ex) {
            throw new BackupException(ex.getMessage());
        } finally {
            if (backupFile != null) {
                backupFile.delete();
            }
        }
    }
}
