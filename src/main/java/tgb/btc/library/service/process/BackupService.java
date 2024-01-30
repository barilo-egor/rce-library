package tgb.btc.library.service.process;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tgb.btc.library.conditional.BackupCondition;
import tgb.btc.library.constants.enums.properties.PropertiesPath;
import tgb.btc.library.exception.BackupException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Service
@Slf4j
@Conditional(BackupCondition.class)
public class BackupService {

    @Value("${spring.datasource.username}")
    public String userDB;

    @Value("${spring.datasource.password}")
    public String passwordDB;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final String CREDENTIALS_FILE_PATH = "src/main/resources/credentials.p12";

    private static final List<String> SCOPES = new ArrayList<>(DriveScopes.all());

    @Async
    public void backup() {
        if (!PropertiesPath.FUNCTIONS_PROPERTIES.getBoolean("auto.backup", Boolean.FALSE)) {
            log.info("Резервное копирование отключено");
            return;
        }
        log.info("Запуск процесса резервного копирования");
        final String database = "rce";
        java.io.File backupFile = null;
        try {
            backupFile = java.io.File.createTempFile("backup", ".sql");
            String command = String.format("mysqldump -u %s -p%s %s --result-file=%s",
                    userDB, passwordDB, database, backupFile.getAbsolutePath());
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();
            sendDrive(backupFile, exitCode);
        } catch (Exception ex) {
            throw new BackupException(ex.getMessage());
        } finally {
            if (backupFile != null) {
                backupFile.delete();
            }
        }
    }

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException, GeneralSecurityException {
        return new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT)
                .setJsonFactory(JSON_FACTORY)
                .setServiceAccountScopes(SCOPES)
                .setServiceAccountPrivateKeyFromP12File(new File(CREDENTIALS_FILE_PATH))
                .setServiceAccountId(PropertiesPath.BACKUP_DRIVE.getString("google.drive.service.account.id"))
                .build();
    }

    public void createGoogleFile(String contentType, String customFileName, byte[] uploadData) {
        AbstractInputStreamContent uploadStreamContent = new ByteArrayContent(contentType, uploadData);
        _createGoogleFile(customFileName, uploadStreamContent);
    }

    private Drive getDrive() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("Google Drive API")
                .build();
    }

    private void _createGoogleFile(String customFileName, AbstractInputStreamContent uploadStreamContent) {
        com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
        fileMetadata.setName(customFileName);
        try {
            Drive service = getDrive();
            FileList list = service.files().list().setQ("name='backup'").setFields("files(id)").execute();
            String parentFolderId = (String) list.getFiles().get(0).get("id");
            List<String> parents = Collections.singletonList(parentFolderId);
            fileMetadata.setParents(parents);
            service.files().create(fileMetadata, uploadStreamContent).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeOldBackup() {
        try {
            Drive service = getDrive();
            FileList list = service.files().list().setQ("name='backup'").setFields("files(id)").execute();
            String parentFolderId = (String) list.getFiles().get(0).get("id");
            long date = LocalDateTime.now().minusDays(7).atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
            String currentDate = new com.google.api.client.util.DateTime(date).toStringRfc3339();
            list = service.files().list().setQ("'" + parentFolderId + "' in parents and createdTime<='" + currentDate + "'")
                    .setFields("files(id, name)").execute();
            for (com.google.api.services.drive.model.File file : list.getFiles()) {
                service.files().delete(file.getId()).execute();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void sendDrive(java.io.File file, int exitCode) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ZipOutputStream out = new ZipOutputStream(bos);
            String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String botUsername = PropertiesPath.BOT_PROPERTIES.getString("bot.username");
            String fileName = botUsername + "_backup_" + timeStamp;
            ZipEntry e = new ZipEntry(fileName + ".sql");
            out.putNextEntry(e);
            byte[] data = java.nio.file.Files.readAllBytes(file.toPath());
            out.write(data, 0, data.length);
            out.closeEntry();
            out.close();
            createGoogleFile("application/zip", fileName + ".zip", new ByteArrayInputStream(bos.toByteArray()).readAllBytes());
            if (exitCode != 0) {
                log.debug("Во время резервного копирования произошла ошибка. Код ответа " + exitCode);
            }
        } catch (Exception e) {
            log.error("Ошибка отправки бэк апа.", e);
            throw new BackupException(e.getMessage());
        }
    }
}
