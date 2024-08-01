package tgb.btc.library.repository.bot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.bot.GroupChat;
import tgb.btc.library.bean.web.api.ApiUser;
import tgb.btc.library.constants.enums.MemberStatus;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.constants.enums.bot.GroupChatType;
import tgb.btc.library.repository.web.ApiUserRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class GroupChatRepositoryTest {

    @Autowired
    private GroupChatRepository groupChatRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ApiUserRepository apiUserRepository;

    private static final long chatId = 12345678L;

    static Stream<Arguments> createGroupChat() {
        GroupChat groupChat = new GroupChat();
        groupChat.setChatId(chatId);
        groupChat.setMemberStatus(MemberStatus.MEMBER);
        groupChat.setRegisterDateTime(LocalDateTime.now());
        groupChat.setType(GroupChatType.DEAL_REQUEST);
        groupChat.setIsSendMessageEnabled(true);
        groupChat.setTitle("title");
        return Stream.of(Arguments.of(groupChat));
    }

    @ParameterizedTest
    @MethodSource("createGroupChat")
    void updateMemberStatusByChatId(GroupChat groupChat) {
        groupChatRepository.save(groupChat);
        entityManager.clear();
        groupChatRepository.updateMemberStatusByChatId(chatId, MemberStatus.ADMINISTRATOR);

        GroupChat actual = groupChatRepository.getById(groupChat.getPid());
        assertEquals(MemberStatus.ADMINISTRATOR, actual.getMemberStatus());
    }

    @ParameterizedTest
    @MethodSource("createGroupChat")
    void updateTitleByChatId(GroupChat groupChat) {
        groupChatRepository.save(groupChat);
        entityManager.clear();
        groupChatRepository.updateTitleByChatId(chatId, "newTitle");

        GroupChat actual = groupChatRepository.getById(groupChat.getPid());
        assertEquals("newTitle", actual.getTitle());
    }

    @ParameterizedTest
    @MethodSource("createGroupChat")
    void dropDealRequestDefault(GroupChat groupChat) {
        groupChatRepository.save(groupChat);
        entityManager.clear();
        groupChatRepository.dropDealRequestDefault();

        GroupChat actual = groupChatRepository.getById(groupChat.getPid());
        assertEquals(GroupChatType.DEFAULT, actual.getType());
    }

    @ParameterizedTest
    @MethodSource("createGroupChat")
    void updateTypeByChatId(GroupChat groupChat) {
        groupChatRepository.save(groupChat);
        entityManager.clear();
        groupChatRepository.updateTypeByChatId(GroupChatType.API_DEAL_REQUEST, chatId);

        GroupChat actual = groupChatRepository.getById(groupChat.getPid());
        assertEquals(GroupChatType.API_DEAL_REQUEST, actual.getType());
    }

    @ParameterizedTest
    @MethodSource("createGroupChat")
    void updateTypeByPid(GroupChat groupChat) {
        groupChatRepository.save(groupChat);
        entityManager.clear();
        groupChatRepository.updateTypeByPid(GroupChatType.DEFAULT, groupChat.getPid());

        GroupChat actual = groupChatRepository.getById(groupChat.getPid());
        assertEquals(GroupChatType.DEFAULT, actual.getType());
    }

    @ParameterizedTest
    @MethodSource("createGroupChat")
    void updateIsSendMessageEnabledByChatId(GroupChat groupChat) {
        groupChatRepository.save(groupChat);
        entityManager.clear();
        groupChatRepository.updateIsSendMessageEnabledByChatId(false, chatId);

        GroupChat actual = groupChatRepository.getById(groupChat.getPid());
        assertFalse(actual.getIsSendMessageEnabled());
    }

    @Test
    void getAllByType() {
        Arrays.stream(GroupChatType.values())
                .forEach(groupChatType -> assertEquals(new ArrayList<>(), groupChatRepository.getAllByType(groupChatType)));

        Map<GroupChatType, Set<GroupChat>> expected = new HashMap<>();
        long chatIdCounter = 1;
        for (GroupChatType groupChatType : GroupChatType.values()) {
            Set<GroupChat> groupChats = new HashSet<>();
            for (long i = 1; i <= 3; i++) {
                GroupChat groupChat = new GroupChat();
                groupChat.setChatId(chatIdCounter++);
                groupChat.setMemberStatus(MemberStatus.MEMBER);
                groupChat.setRegisterDateTime(LocalDateTime.now());
                groupChat.setType(groupChatType);
                groupChat.setIsSendMessageEnabled(true);
                groupChat.setTitle("title");
                groupChats.add(groupChatRepository.save(groupChat));
            }
            expected.put(groupChatType, groupChats);
        }

        for (Map.Entry<GroupChatType, Set<GroupChat>> entry : expected.entrySet()) {
            assertEquals(entry.getValue(), new HashSet<>(groupChatRepository.getAllByType(entry.getKey())));
        }
    }

    @Test
    void getByChatId() {
        Map<Long, GroupChat> expected = new HashMap<>();
        for (long i = 1; i <= 3; i++) {
            GroupChat groupChat = new GroupChat();
            groupChat.setChatId(i);
            groupChat.setMemberStatus(MemberStatus.MEMBER);
            groupChat.setRegisterDateTime(LocalDateTime.now());
            groupChat.setType(GroupChatType.DEFAULT);
            groupChat.setIsSendMessageEnabled(true);
            groupChat.setTitle("title");
            expected.put(i, groupChatRepository.save(groupChat));
            i++;
        }

        for (Map.Entry<Long, GroupChat> entry : expected.entrySet()) {
            GroupChat actual = groupChatRepository.getByChatId(entry.getKey());
            assertEquals(entry.getValue(), actual);
        }
        assertNull(groupChatRepository.getByChatId(Long.MAX_VALUE));
    }

    @Test
    void getPidByChatId() {
        Map<Long, Long> expected = new HashMap<>();
        for (long i = 1; i <= 3; i++) {
            GroupChat groupChat = new GroupChat();
            groupChat.setChatId(i);
            groupChat.setMemberStatus(MemberStatus.MEMBER);
            groupChat.setRegisterDateTime(LocalDateTime.now());
            groupChat.setType(GroupChatType.DEFAULT);
            groupChat.setIsSendMessageEnabled(true);
            groupChat.setTitle("title");
            expected.put(i, groupChatRepository.save(groupChat).getPid());
            i++;
        }

        for (Map.Entry<Long, Long> entry : expected.entrySet()) {
            Long actual = groupChatRepository.getPidByChatId(entry.getKey());
            assertEquals(entry.getValue(), actual);
        }
        assertNull(groupChatRepository.getPidByChatId(Long.MAX_VALUE));
    }

    @Test
    void countByTypeAndChatId() {
        Map<Long, Long> expected = new HashMap<>();
        long chatIdCounter = 1;
        for (GroupChatType groupChatType : GroupChatType.values()) {
            GroupChat groupChat = new GroupChat();
            groupChat.setChatId(chatIdCounter);
            groupChat.setMemberStatus(MemberStatus.MEMBER);
            groupChat.setRegisterDateTime(LocalDateTime.now());
            groupChat.setType(groupChatType);
            groupChat.setIsSendMessageEnabled(true);
            groupChat.setTitle("title");
            groupChatRepository.save(groupChat);
            chatIdCounter++;
        }
        chatIdCounter = 1;
        for (GroupChatType groupChatType: GroupChatType.values()) {
            assertEquals(1, groupChatRepository.countByTypeAndChatId(groupChatType, chatIdCounter));
            groupChatRepository.delete(groupChatRepository.getByChatId(chatIdCounter));
            assertEquals(0, groupChatRepository.countByTypeAndChatId(groupChatType, chatIdCounter));
            chatIdCounter++;
        }
    }

    @ParameterizedTest
    @MethodSource("createGroupChat")
    void getByApiUserPid(GroupChat groupChat) {
        groupChatRepository.save(groupChat);
        ApiUser apiUser = new ApiUser();
        apiUser.setId("1");
        apiUser.setToken("11");
        apiUser.setFiatCurrency(FiatCurrency.BYN);
        apiUser.setGroupChat(groupChat);
        apiUserRepository.save(apiUser);
        Optional<GroupChat> actual = groupChatRepository.getByApiUserPid(apiUser.getPid());
        assertTrue(actual.isPresent());
        assertEquals(groupChat, actual.get());
    }

    @Test
    void getByApiUserPidWithoutGroupChat() {
        ApiUser apiUser = new ApiUser();
        apiUser.setId("1");
        apiUser.setToken("11");
        apiUser.setFiatCurrency(FiatCurrency.BYN);
        apiUserRepository.save(apiUser);
        assertTrue(groupChatRepository.getByApiUserPid(apiUser.getPid()).isEmpty());
    }

    @ParameterizedTest
    @MethodSource("createGroupChat")
    void getPidByApiUserPid(GroupChat groupChat) {
        groupChatRepository.save(groupChat);
        ApiUser apiUser = new ApiUser();
        apiUser.setId("1");
        apiUser.setToken("11");
        apiUser.setFiatCurrency(FiatCurrency.BYN);
        apiUser.setGroupChat(groupChat);
        apiUserRepository.save(apiUser);
        Optional<Long> actual = groupChatRepository.getPidByApiUserPid(apiUser.getPid());
        assertTrue(actual.isPresent());
        assertEquals(groupChat.getPid(), actual.get());
    }

    @Test
    void getPidByApiUserPidWithoutGroup() {
        ApiUser apiUser = new ApiUser();
        apiUser.setId("1");
        apiUser.setToken("11");
        apiUser.setFiatCurrency(FiatCurrency.BYN);
        apiUserRepository.save(apiUser);
        assertTrue(groupChatRepository.getPidByApiUserPid(apiUser.getPid()).isEmpty());
    }
}