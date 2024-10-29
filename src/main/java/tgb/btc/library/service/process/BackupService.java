package tgb.btc.library.service.process;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tgb.btc.library.conditional.BackupCondition;
import tgb.btc.library.exception.BackupException;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.Consumer;


@Service
@Slf4j
@Conditional(BackupCondition.class)
public class BackupService {

    @Value("${spring.datasource.username}")
    private String userDB;

    @Value("${spring.datasource.password}")
    private String passwordDB;

    @Async
    public void backup(Consumer<File> consumer) {
        log.info("Запуск процесса резервного копирования");
        final String database = "rce";
        File tempBackupFile = null;
        File splitDirectory = null;

        try {
            // Создаем временный файл для основного бэкапа
            tempBackupFile = File.createTempFile("backup-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm_")), ".sql");

            // Команда для создания бэкапа базы данных
            String backupCommand = String.format("mysqldump -u%s -p%s %s --result-file=%s",
                    userDB, passwordDB, database, tempBackupFile.getAbsolutePath());

            // Запускаем команду mysqldump
            Process backupProcess = Runtime.getRuntime().exec(backupCommand);
            backupProcess.waitFor();

            // Проверка на успешное завершение бэкапа
            if (backupProcess.exitValue() != 0) {
                throw new BackupException("Ошибка выполнения mysqldump");
            }

            // Создаем временную директорию для хранения частей
            splitDirectory = Files.createTempDirectory("backup_split").toFile();

            // Команда для разбиения файла на части по 50 МБ
            String splitCommand = String.format("split -b 50M %s %s/backup_part_",
                    tempBackupFile.getAbsolutePath(), splitDirectory.getAbsolutePath());

            // Запускаем команду split
            Process splitProcess = Runtime.getRuntime().exec(splitCommand);
            splitProcess.waitFor();

            // Проверка на успешное завершение split
            if (splitProcess.exitValue() != 0) {
                throw new BackupException("Ошибка выполнения split");
            }

            // Передаем все части файла через consumer
            for (File splitFile : Objects.requireNonNull(splitDirectory.listFiles())) {
                consumer.accept(splitFile);
            }
        } catch (Exception ex) {
            throw new BackupException(ex.getMessage());
        } finally {
            // Удаляем временные файлы
            if (tempBackupFile != null) {
                tempBackupFile.delete();
            }
            if (splitDirectory != null) {
                for (File file : Objects.requireNonNull(splitDirectory.listFiles())) {
                    file.delete();
                }
                splitDirectory.delete();
            }
        }
    }
}

