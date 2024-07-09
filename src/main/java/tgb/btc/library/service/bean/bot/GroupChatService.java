package tgb.btc.library.service.bean.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.GroupChat;
import tgb.btc.library.constants.enums.MemberStatus;
import tgb.btc.library.interfaces.service.bean.bot.IGroupChatService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.GroupChatRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
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
    public GroupChat register(Long chatId, String title, MemberStatus memberStatus) {
        Boolean isDefault = null;
        if (groupChatRepository.count() == 0)
            isDefault = true;
        return groupChatRepository.save(GroupChat.builder()
                .chatId(chatId)
                .memberStatus(memberStatus)
                .title(title)
                .isDefault(isDefault)
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
    @Transactional
    public void setDefaultByPid(Long pid) {
        groupChatRepository.dropDefault();
        groupChatRepository.setDefaultByPid(pid);
    }

    @Override
    public Optional<GroupChat> getDefault() {
        return groupChatRepository.getDefault();
    }

    @Override
    public boolean hasDefault() {
        return groupChatRepository.getDefault().isPresent();
    }

    @Override
    public void deleteByChatId(Long chatId) {
        groupChatRepository.delete(groupChatRepository.getByChatId(chatId));
    }

    @Override
    public void updateIsSendMessageEnabledByChatId(Boolean isSendMessageEnabled, Long chatId) {
        groupChatRepository.updateIsSendMessageEnabledByChatId(isSendMessageEnabled, chatId);
    }
}
