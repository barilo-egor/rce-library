package tgb.btc.library.service.process;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tgb.btc.library.conditional.BackupCondition;
import tgb.btc.library.constants.enums.properties.PropertiesPath;
import tgb.btc.library.exception.BackupException;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
@Conditional(BackupCondition.class)
public class BackupService {

    @Value("${spring.datasource.username}")
    public String userDB;

    @Value("${spring.datasource.password}")
    public String passwordDB;

    private JavaMailSender javaMailSender;

    @Autowired(required = false)
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void backup() {
        if (!PropertiesPath.FUNCTIONS_PROPERTIES.getBoolean("auto.backup", Boolean.FALSE)) {
            log.info("Резервное копирование отключено");
            return;
        }
        log.info("Запуск процесса резервного копирования");
        final String database = "rce";
        File backupFile = null;
        try {
            backupFile = File.createTempFile("backup", "sql");
            String command = String.format("mysqldump -u %s -p%s %s --result-file=%s",
                    userDB, passwordDB, database, backupFile.getAbsolutePath());
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();
            sendMail(backupFile, exitCode);
        } catch (Exception ex) {
            throw new BackupException(ex.getMessage());
        } finally {
            if (backupFile != null) {
                backupFile.delete();
            }
        }
    }

    private void sendMail(File file, int exitCode) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, Boolean.TRUE);
            helper.setTo(PropertiesPath.BACKUP_MAILS.getString("backup.mails").split(";"));
            helper.setSubject("Резервная копия БД");
            helper.setText(StringUtils.EMPTY);
            if (exitCode != 0) {
                helper.setText("Во время резервного копирования произошла ошибка. Код ответа " + exitCode);
            } else {
                FileSystemResource resource = new FileSystemResource(file);
                String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
                helper.addAttachment("backup_" + timeStamp + ".sql", resource);
            }
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new BackupException(e.getMessage());
        }
    }
}
