package tgb.btc.library.service.stub;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import tgb.btc.api.bot.IFileDownloader;

import java.io.File;

/**
 * Заглушка для тестов. Настоящая реализация в rce.
 */
@Profile({"test", "web"})
@Service
public class FileDownloaderStub implements IFileDownloader {

    @Override
    public void downloadFile(String s, String s1) {
        // stub
    }

    @Override
    public String saveFile(File file) {
        return null;
    }

    @Override
    public String saveFile(File file, boolean b) {
        return null;
    }
}
