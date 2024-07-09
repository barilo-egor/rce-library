package tgb.btc.library.service.bean.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.GroupChat;
import tgb.btc.library.constants.enums.MemberStatus;
import tgb.btc.library.constants.enums.bot.GroupChatType;
import tgb.btc.library.interfaces.service.bean.bot.IGroupChatService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.GroupChatRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GroupChatService extends BasePersistService<GroupChat> implements IGroupChatService {

    private GroupChatRepository groupChatRepository;

    @Autowired
    public void setGroupChatRepository(GroupChatRepository groupChatRepository) {
        this.groupChatRepository = groupChatRepository;
    }

    @Override
    protected BaseRepository<GroupChat> getBaseRepository() {
        return groupChatRepository;
    }

    @Override
    @Transactional
    public GroupChat register(Long chatId, String title, MemberStatus memberStatus, GroupChatType groupChatType) {
        if (GroupChatType.DEAL_REQUEST.equals(groupChatType) && hasDealRequests()) {
            log.debug("Обнаружено несколько " + GroupChatType.DEAL_REQUEST.name()
                    + " групп при создании новой. Будет установлена группа chatid={}", chatId);
            dropDealRequestDefault();
        }
        return groupChatRepository.save(GroupChat.builder()
                .chatId(chatId)
                .memberStatus(memberStatus)
                .title(title)
                .type(groupChatType)
                .registerDateTime(LocalDateTime.now())
                .isSendMessageEnabled(true)
                .build());
    }

    @Override
    public boolean isExists(Long chatId) {
        return groupChatRepository.exists(Example.of(GroupChat.builder()
                .chatId(chatId)
                .build()));
    }

    @Override
    public Optional<GroupChat> find(Long chatId) {
        return groupChatRepository.findOne(Example.of(GroupChat.builder()
                .chatId(chatId)
                .build()));
    }

    @Override
    public void updateMemberStatus(Long chatId, MemberStatus memberStatus) {
        groupChatRepository.updateMemberStatusByChatId(chatId, memberStatus);
    }

    @Override
    public void updateTitleByChatId(Long chatId, String title) {
        groupChatRepository.updateTitleByChatId(chatId, title);
    }

    @Override
    public void deleteByChatId(Long chatId) {
        groupChatRepository.delete(groupChatRepository.getByChatId(chatId));
    }

    @Override
    public void updateIsSendMessageEnabledByChatId(Boolean isSendMessageEnabled, Long chatId) {
        groupChatRepository.updateIsSendMessageEnabledByChatId(isSendMessageEnabled, chatId);
    }

    @Override
    public Optional<GroupChat> getByType(GroupChatType type) {
        return groupChatRepository.getByType(type);
    }

    @Override
    @Transactional
    public void updateTypeByChatId(GroupChatType type, Long chatId) {
        if (GroupChatType.DEAL_REQUEST.equals(type) && hasDealRequests()) {
            log.debug("Обнаружено несколько " + GroupChatType.DEAL_REQUEST.name() + " групп. "
                    + "Будет установлена группа chatid={}", chatId);
            dropDealRequestDefault();
        }
        groupChatRepository.updateTypeByChatId(type, chatId);
    }

    @Override
    @Transactional
    public void updateTypeByPid(GroupChatType type, Long pid) {
        if (GroupChatType.DEAL_REQUEST.equals(type) && hasDealRequests()) {
            log.debug("Обнаружено несколько " + GroupChatType.DEAL_REQUEST.name() + " групп. "
                    + "Будет установлена группа pid={}", pid);
            dropDealRequestDefault();
        }
        groupChatRepository.updateTypeByPid(type, pid);
    }

    @Override
    public void dropDealRequestDefault() {
        groupChatRepository.dropDealRequestDefault();
    }

    @Override
    public boolean hasDealRequests() {
        return groupChatRepository.exists(Example.of(GroupChat.builder()
                .type(GroupChatType.DEAL_REQUEST)
                .build()));
    }

    @Override
    public List<GroupChat> getAllByType(GroupChatType type) {
        return groupChatRepository.getAllByType(type);
    }

    @Override
    public boolean existsByChatId(Long chatId) {
        return groupChatRepository.exists(Example.of(GroupChat.builder()
                .chatId(chatId)
                .build()));
    }
}
