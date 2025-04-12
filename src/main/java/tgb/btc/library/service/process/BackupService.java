package tgb.btc.library.service.process;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tgb.btc.library.exception.BackupException;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.Consumer;


@Service
@Slf4j
@ConditionalOnProperty(name = "auto.backup", havingValue = "true", matchIfMissing = false)
public class BackupService {

    @Value("${spring.datasource.username}")
    private String userDB;

    @Value("${spring.datasource.password}")
    private String passwordDB;

    @Value("${database.name}")
    private String databaseName;

    @Async
    public void backup(Consumer<File> consumer) {
        log.info("Запуск процесса резервного копирования");
        File tempBackupFile = null;
        File splitDirectory = null;

        try {
            // Формируем имя файла с текущей датой в формате "backup_<дата>.sql"
            String backupFileName = "backup_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm"));
            tempBackupFile = File.createTempFile(backupFileName, ".sql");

            // Команда для создания бэкапа базы данных
            String backupCommand = String.format("mysqldump -h mysql -u%s -p%s %s --result-file=%s",
                    userDB, passwordDB, databaseName, tempBackupFile.getAbsolutePath());

            // Запускаем команду mysqldump
            Process backupProcess = Runtime.getRuntime().exec(backupCommand);
            backupProcess.waitFor();

            // Проверка на успешное завершение бэкапа
            if (backupProcess.exitValue() != 0) {
                log.error("Ошибка бэкапа: exitValue={}", backupProcess.exitValue());
                throw new BackupException("Ошибка выполнения mysqldump");
            }

            // Создаем временную директорию для хранения частей
            splitDirectory = Files.createTempDirectory("backup_split").toFile();

            // Изменение: команда для разбиения файла на части по 45 МБ с добавлением нижнего подчеркивания перед суффиксом
            String splitCommand = String.format("split -b 45M %s %s/%s_part_",
                    tempBackupFile.getAbsolutePath(), splitDirectory.getAbsolutePath(), backupFileName);

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
            log.error(ex.getMessage(), ex);
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

