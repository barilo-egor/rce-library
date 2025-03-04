package tgb.btc.library.service.design;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tgb.btc.api.bot.IFileDownloader;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.enums.MessageImage;
import tgb.btc.library.interfaces.service.design.IMessageImageService;
import tgb.btc.library.vo.properties.FileIdContainer;
import tgb.btc.library.vo.properties.FileIdsContainer;
import tgb.btc.library.vo.properties.ImageMessages;
import tgb.btc.library.vo.properties.MessageVariable;

import java.io.*;
import java.util.*;

@Service
@Slf4j
public class MessageImageService implements IMessageImageService {

    private static final String FILE_IDS_JSON_PATH = "config/design/message_images/fileIds.json";

    private static final String HELP_FILE_PATH = "config/design/message_images/help.txt";

    private static final List<String> FORMATS = List.of(".png", ".jpg", ".jpeg", ".mp4", ".gif");

    private static final String IMAGE_PATH = "config/design/message_images/%s%s";

    private static final String MESSAGES_JSON_PATH = "config/design/message_images/messages.json";

    private final IFileDownloader fileDownloader;

    private final CacheManager cacheManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<MessageImage, MessageVariable> messages = new EnumMap<>(MessageImage.class);

    private final Map<MessageImage, FileIdContainer> fileIds = new EnumMap<>(MessageImage.class);

    private FileIdsContainer fileIdsContainers;

    @Autowired
    public MessageImageService(IFileDownloader fileDownloader, CacheManager cacheManager) {
        this.fileDownloader = fileDownloader;
        this.cacheManager = cacheManager;
    }

    @PostConstruct
    public void init() throws IOException {
        loadText();
        loadFileIds();
        loadHelp();
    }

    private void loadHelp() {
        File helpFile = new File(HELP_FILE_PATH);
        if (!helpFile.exists()) {
            log.debug("help.txt для изображений к сообщениям отсутствует и будет создан.");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(HELP_FILE_PATH))) {
                writer.write("В папке config/design/message_images должны лежать изображения со следующими наименованиями:\n\n");
                for (MessageImage value : MessageImage.values()) {
                    writer.write(value.name() + " - " + value.getDescription());
                    writer.newLine();  // Переход на новую строку
                }
                writer.write("Доступные форматы на текущий момент: " + String.join(" ", FORMATS));
                writer.write("""
                        
                        В случае отсутствия изображения будет отправляться только текст.
                        
                        
                        В папке config/design/message_images также должен лежать \
                        messages.json(создается из заполняется автоматически при старте приложения)\
                         с аналогичными названиями проперти и текстами сообщений. Перенос строк обозначается как \\n
                        После обновления уже существующей картинки следует удалить файл fileIds.json
                        
                        Данный файл help.txt создается автоматически при старте приложения. Его можно удалить и перезапустить\
                         бота, чтобы пересоздать файл и получить актуальные данные.""");
                log.debug("help.txt успешно создан.");
            } catch (IOException e) {
                log.debug("Ошибка при создании help.txt", e);
            }
        }
    }

    private void loadFileIds() throws IOException {
        File fileFilesIds = new File(FILE_IDS_JSON_PATH);
        if (!fileFilesIds.exists()) {
            log.debug("Файл {} для хранения fileId изображений для сообщений не найден. Будет создан новый.", FILE_IDS_JSON_PATH);
            if (!fileFilesIds.createNewFile()) {
                log.error("Не получилось создать файл {}.", MESSAGES_JSON_PATH);
                throw new BaseException();
            }
            FileIdsContainer fileIdsContainersFromFile = new FileIdsContainer();
            fileIdsContainersFromFile.setFileIds(new ArrayList<>());
            ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
            writer.writeValue(fileFilesIds, fileIdsContainersFromFile);
        }

        fileIdsContainers = objectMapper.readValue(fileFilesIds, FileIdsContainer.class);

        for (MessageImage messageImage : MessageImage.values()) {
            Optional<FileIdContainer> optionalMessageVariable = fileIdsContainers.getFileIds()
                    .stream()
                    .filter(messageVariable -> messageVariable.getType().equals(messageImage))
                    .findFirst();
            if (optionalMessageVariable.isPresent()) {
                FileIdContainer messageVariable = optionalMessageVariable.get();
                if (!StringUtils.isBlank(messageVariable.getFileId())) {
                    fileIds.put(messageImage, messageVariable);
                    log.debug("FileId для {} найден и загружен в кеш.", messageImage.name());
                }
            }
        }
    }

    private void loadText() throws IOException {
        File messagesFile = new File(MESSAGES_JSON_PATH);
        if (!messagesFile.exists()) {
            log.debug("Отсутствует файл с текстами сообщений {} , будет создан новый.", MESSAGES_JSON_PATH);
            if (!messagesFile.createNewFile()) {
                log.error("Не получилось создать файл {}.", MESSAGES_JSON_PATH);
                throw new BaseException();
            }
            ImageMessages imageMessages = new ImageMessages();
            imageMessages.setMessages(new ArrayList<>());
            objectMapper.writeValue(messagesFile, imageMessages);
        }
        ImageMessages messagesFromFile;
        try {
            messagesFromFile = objectMapper.readValue(messagesFile, ImageMessages.class);
        } catch (Exception e) {
            log.error("Ошибка при попытке считать {}", MESSAGES_JSON_PATH);
            log.error(e.getMessage(), e);
            throw new BaseException(e.getMessage(), e);
        }
        for (MessageImage messageImage : MessageImage.values()) {
            Optional<MessageVariable> optionalMessageVariable = messagesFromFile.getMessages()
                    .stream()
                    .filter(messageVariable -> messageVariable.getType().equals(messageImage))
                    .findFirst();
            if (optionalMessageVariable.isPresent()) {
                MessageVariable messageVariable = optionalMessageVariable.get();
                if (StringUtils.isBlank(messageVariable.getText())) {
                    this.messages.put(messageImage, MessageVariable.builder().type(messageImage).text(messageImage.getDefaultMessage()).build());
                    messageVariable.setText(messageImage.getDefaultMessage());
                    log.debug("У сообщения {} отсутствует текст. Будет установлен дефолтный.", messageImage.name());
                } else {
                    this.messages.put(messageImage, messageVariable);
                }
            } else {
                this.messages.put(messageImage, MessageVariable.builder().type(messageImage).text(messageImage.getDefaultMessage()).build());
                messagesFromFile.getMessages().add(MessageVariable.builder()
                        .type(messageImage)
                        .text(messageImage.getDefaultMessage())
                        .build());
                log.debug("Отсутствует {} . Сообщение с дефолтным текстом будет добавлено.", messageImage.name());
            }
        }
        try {
            ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
            objectWriter.writeValue(messagesFile, messagesFromFile);
        } catch (Exception e) {
            log.error("Ошибка при попытке записи {} значения: {}", MESSAGES_JSON_PATH, messagesFromFile);
            log.error(e.getMessage(), e);
            throw new BaseException(e.getMessage(), e);
        }
    }

    @Override
    @Cacheable("messageImageFilesIdsCache")
    public String getFileId(MessageImage messageImage) {
        FileIdContainer fileId = fileIds.get(messageImage);
        if (Objects.nonNull(fileId) && StringUtils.isNotBlank(fileId.getFileId())) {
            return fileId.getFileId();
        } else {
            File imageFile = null;
            String fileFormat = null;
            for (String format : FORMATS) {
                imageFile = new File(String.format(IMAGE_PATH, messageImage.name(), format));
                if (imageFile.exists()) {
                    fileFormat = format;
                    break;
                } else {
                    imageFile = null;
                }
            }
            if (Objects.isNull(imageFile)) {
                return null;
            }
            String strFileId = fileDownloader.saveFile(imageFile, false);
            fileId = FileIdContainer.builder().type(messageImage).format(fileFormat).fileId(strFileId).build();
            fileIdsContainers.getFileIds().add(fileId);
            fileIds.put(messageImage, fileId);
            ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
            try {
                objectWriter.writeValue(new File(FILE_IDS_JSON_PATH), fileIdsContainers);
            } catch (IOException e) {
                log.error("Ошибка при попытке записи fileId для {}.", messageImage.name());
                log.error(e.getMessage(), e);
                throw new BaseException(e.getMessage(), e);
            }
            log.debug("Было загружено новое изображение для {}.", messageImage.name());
            return strFileId;
        }
    }

    @Override
    @Cacheable("messageImageMessagesCache")
    public String getMessage(MessageImage messageImage) {
        MessageVariable value = messages.get(messageImage);
        return Objects.isNull(value) || StringUtils.isBlank(value.getText())
                ? messageImage.getDefaultMessage()
                : value.getText();
    }

    @Override
    @Cacheable("messageImageSubTypesCache")
    public Integer getSubType(MessageImage messageImage) {
        MessageVariable value = messages.get(messageImage);
        return Objects.isNull(value) || Objects.isNull(value.getSubType())
                ? 1
                : value.getSubType();
    }

    @Override
    @Cacheable("messageImageFormatCache")
    public String getFormat(MessageImage messageImage) {
        FileIdContainer fileId = fileIds.get(messageImage);
        return Objects.nonNull(fileId)
                ? fileId.getFormat()
                : ".jpg";
    }

    @Override
    public String getFormatNullable(MessageImage messageImage) {
        String result = null;
        for (String format : FORMATS) {
            File imageFile = new File(String.format(IMAGE_PATH, messageImage.name(), format));
            if (imageFile.exists()) {
                result = format;
                break;
            }
        }
        return result;
    }

    @Override
    public File getFile(MessageImage messageImage) {
        File imageFile = null;
        for (String format : FORMATS) {
            imageFile = new File(String.format(IMAGE_PATH, messageImage.name(), format));
            if (imageFile.exists()) {
                break;
            } else {
                imageFile = null;
            }
        }
        return imageFile;
    }

    @Override
    public void updateText(MessageImage messageImage, String text) {
        File messagesFile = new File(MESSAGES_JSON_PATH);
        ImageMessages messagesFromFile;
        try {
            messagesFromFile = objectMapper.readValue(messagesFile, ImageMessages.class);
        } catch (Exception e) {
            log.error("Ошибка при попытке считать {} для обновления", MESSAGES_JSON_PATH);
            log.error(e.getMessage(), e);
            throw new BaseException(e.getMessage(), e);
        }
        messages.get(messageImage).setText(text);
        messagesFromFile.getMessages().stream()
                .filter(messageVariable -> messageVariable.getType().equals(messageImage))
                .findFirst()
                .ifPresent(messageVariable -> messageVariable.setText(text));
        try {
            ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
            objectWriter.writeValue(messagesFile, messagesFromFile);
        } catch (Exception e) {
            log.error("Ошибка при попытке обновления {} значения: {}", MESSAGES_JSON_PATH, messages);
            log.error(e.getMessage(), e);
            throw new BaseException(e.getMessage(), e);
        }
        Optional.ofNullable(cacheManager.getCache("messageImageMessagesCache")).ifPresent(Cache::clear);
    }

    @Override
    public void setImage(MessageImage messageImage, MultipartFile multipartFile) {
        if (Objects.isNull(multipartFile.getOriginalFilename())) {
            throw new BaseException("Отсутствует originalFilename у файла.");
        }
        String filePath = String.format(IMAGE_PATH, messageImage.name(),
                multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")));
        try {
            File destination = new File(filePath);
            try (InputStream inputStream = multipartFile.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(destination)) {

                byte[] buffer = new byte[8192];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        } catch (IOException e) {
            log.error("Ошибка при сохранении файла для сообщения: {}", e.getMessage(), e);
            throw new BaseException("Ошибка при сохранении файла для сообщения.");
        }
        fileIdsContainers.getFileIds().removeIf(fileId -> messageImage.equals(fileId.getType()));
        fileIds.remove(messageImage);
        Optional.ofNullable(cacheManager.getCache("messageImageFilesIdsCache")).ifPresent(Cache::clear);
        Optional.ofNullable(cacheManager.getCache("messageImageFormatCache")).ifPresent(Cache::clear);
    }

    @Override
    public void deleteImage(MessageImage messageImage) {
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        try {
            File imageFile = null;
            for (String format : FORMATS) {
                imageFile = new File(String.format(IMAGE_PATH, messageImage.name(), format));
                if (imageFile.exists()) {
                    break;
                } else {
                    imageFile = null;
                }
            }
            if (Objects.isNull(imageFile)) {
                throw new BaseException("Файл не найден.");
            }
            if (!imageFile.delete()) {
                throw new BaseException("Файл не был удален для " + messageImage.name());
            }
            fileIds.remove(messageImage);
            fileIdsContainers.getFileIds().removeIf(fileId -> messageImage.equals(fileId.getType()));
            objectWriter.writeValue(new File(FILE_IDS_JSON_PATH), fileIdsContainers);
        } catch (IOException e) {
            log.error("Ошибка при попытке удаления {}.", messageImage.name());
            log.error(e.getMessage(), e);
            throw new BaseException(e.getMessage(), e);
        }
        Optional.ofNullable(cacheManager.getCache("messageImageFilesIdsCache")).ifPresent(Cache::clear);
        Optional.ofNullable(cacheManager.getCache("messageImageFormatCache")).ifPresent(Cache::clear);
    }
}
