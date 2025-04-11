package tgb.btc.library.service.util;

import org.springframework.stereotype.Service;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.util.IBufferFileService;

import java.io.File;

@Service
public class BufferFileService implements IBufferFileService {

    public static final String PATH = "buffer";

    public String getPath() {
        File file = new File(PATH);
        if (!file.exists()) {
            if (!file.mkdir()) {
                throw new BaseException("Ошибка создания buffer директории.");
            }
        }
        return PATH;
    }
}
