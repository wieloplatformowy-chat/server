package net.chat.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import net.chat.config.authentication.TokenService;
import net.chat.entity.ConversationEntity;
import net.chat.entity.MessageEntity;
import net.chat.entity.UserConversationEntity;
import net.chat.entity.UserEntity;
import net.chat.repository.ConversationDao;
import net.chat.repository.FriendDao;
import net.chat.repository.MessageDao;
import net.chat.repository.UserConversationDao;
import net.chat.rest.dto.MessageResponse;
import net.chat.rest.dto.SendMessageParams;
import net.chat.rest.message.Errors;
import net.chat.rest.message.ResponseError;
import net.chat.service.FriendService;
import net.chat.service.MessageService;
import net.chat.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Mariusz Gorzycki
 * @since 26.05.2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {MockTestConfiguration.class}, loader = AnnotationConfigWebContextLoader.class)
@Transactional
public class MessageRestControllerTest extends BaseRestControllerTest {
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "test@test.pl";
    private static final String CONVERSATION_NAME = "ConversationName";
    private static final String MESSAGE = "Message";

    @Autowired
    private MessageRestController objectUnderTest;

    @Autowired
    TokenService tokenService;

    @Autowired
    FriendService friendService;

    @Autowired
    FriendDao friendDao;

    @Autowired
    UserService userService;

    @Autowired
    ConversationDao conversationDao;

    @Autowired
    UserConversationDao userConversationDao;

    @Autowired
    MessageService messageService;

    @Autowired
    MessageDao messageDao;

    @Override
    public MessageRestController getObjectUnderTest() {
        return objectUnderTest;
    }

    @Test
    public void sendMessageInvalidAndNoAuthHeader() throws Exception {
        testInvalidAndNullToken(post("/messages/send"), new SendMessageParams());
    }

    @Test
    public void sendMessageNullCredentials() throws Exception {
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(logged);
        testNullCredentials(post("/messages/send"), new SendMessageParams(), token);
    }

    @Test
    public void sendMessage() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(logged);

        ConversationEntity conversationEntity = new ConversationEntity().setName(CONVERSATION_NAME).setUsers(logged);
        conversationDao.persist(conversationEntity);

        SendMessageParams params = new SendMessageParams().setConversationId(conversationEntity.getId()).setMessage(MESSAGE);

        //when
        ResultActions result = mock.perform(post("/messages/send").content(toJson(params)).contentType(MediaType.APPLICATION_JSON).header(AUTH_TOKEN_HEADER, token));

        //then
        result.andExpect(status().isOk());
        MessageEntity message = messageDao.findAll().get(0);
        assertThat(message.getMessage()).isEqualTo(MESSAGE);
        assertThat(message.getUser()).isEqualTo(logged);
        assertThat(message.getConversation()).isEqualTo(conversationEntity);
        assertThat(message.getDate()).isNotNull();
    }

    @Test
    public void sendMessageNotExistingGroup() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(logged);
        Long notExistingConversationId = 123L;

        SendMessageParams params = new SendMessageParams().setConversationId(notExistingConversationId).setMessage(MESSAGE);

        //when
        ResultActions result = mock.perform(post("/messages/send").content(toJson(params)).contentType(MediaType.APPLICATION_JSON).header(AUTH_TOKEN_HEADER, token));
        ResponseError response = responseFromJson(result, ResponseError.class);

        //then
        result.andExpect(status().isBadRequest());
        assertThat(response.getId()).isEqualTo(Errors.CONVERSATION_NOT_EXISTS.getId());
    }

    @Test
    public void sendMessageNotMyGroup() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        UserEntity user1 = registerUser("user1", EMAIL, PASSWORD);
        String token = userService.login(logged);

        ConversationEntity conversationEntity = new ConversationEntity().setName(CONVERSATION_NAME).setUsers(user1);
        conversationDao.persist(conversationEntity);

        SendMessageParams params = new SendMessageParams().setConversationId(conversationEntity.getId()).setMessage(MESSAGE);

        //when
        ResultActions result = mock.perform(post("/messages/send").content(toJson(params)).contentType(MediaType.APPLICATION_JSON).header(AUTH_TOKEN_HEADER, token));
        ResponseError response = responseFromJson(result, ResponseError.class);

        //then
        result.andExpect(status().isBadRequest());
        assertThat(response.getId()).isEqualTo(Errors.CONVERSATION_NOT_EXISTS.getId());
    }

    @Test
    public void getLastMessagesInvalidAndNoAuthHeader() throws Exception {
        testInvalidAndNullToken(get("/messages/last/1"));
    }

    @Test
    public void lastMessages() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(logged);

        ConversationEntity conversationEntity = new ConversationEntity().setName(CONVERSATION_NAME).setUsers(logged);
        conversationDao.persist(conversationEntity);
        createMessages(logged, conversationEntity, 25);

        //when
        ResultActions result = mock.perform(get("/messages/last/" + conversationEntity.getId()).header(AUTH_TOKEN_HEADER, token));
        List<MessageResponse> messages = responseFromJson(result, new TypeReference<List<MessageResponse>>() {
        });

        //then
        result.andExpect(status().isOk());
        assertThat(messages).hasSize(20);
        assertThat(messages.get(0).getMessage()).isEqualTo(MESSAGE + 6);
        assertThat(messages.get(19).getMessage()).isEqualTo(MESSAGE + 25);

        UserConversationEntity userConversation = userConversationDao.findByUserAndConversation(logged, conversationEntity);
        assertThat(userConversation.getLastSeenPost().getMessage()).isEqualTo(MESSAGE + 25);
        assertThat(userConversation.getLastSeenPostDate()).isNotNull();
    }

    @Test
    public void lastMessagesEmpty() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(logged);

        ConversationEntity conversationEntity = new ConversationEntity().setName(CONVERSATION_NAME).setUsers(logged);
        conversationDao.persist(conversationEntity);

        //when
        ResultActions result = mock.perform(get("/messages/last/" + conversationEntity.getId()).header(AUTH_TOKEN_HEADER, token));
        List<MessageResponse> messages = responseFromJson(result, new TypeReference<List<MessageResponse>>() {
        });

        //then
        UserConversationEntity userConversation = userConversationDao.findByUserAndConversation(logged, conversationEntity);
        result.andExpect(status().isOk());
        assertThat(messages).isEmpty();
    }

    @Test
    public void lastMessagesConversationNotExists() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(logged);
        Long notExistingConversationId = 123L;

        //when
        ResultActions result = mock.perform(get("/messages/last/" + notExistingConversationId).header(AUTH_TOKEN_HEADER, token));
        ResponseError response = responseFromJson(result, ResponseError.class);

        //then
        result.andExpect(status().isBadRequest());
        assertThat(response.getId()).isEqualTo(Errors.CONVERSATION_NOT_EXISTS.getId());
    }

    @Test
    public void lastMessagesNotMyConversation() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        UserEntity user1 = registerUser("user1", EMAIL, PASSWORD);
        String token = userService.login(logged);

        ConversationEntity conversationEntity = new ConversationEntity().setName(CONVERSATION_NAME).setUsers(user1);
        conversationDao.persist(conversationEntity);

        //when
        ResultActions result = mock.perform(get("/messages/last/" + conversationEntity.getId()).header(AUTH_TOKEN_HEADER, token));
        ResponseError response = responseFromJson(result, ResponseError.class);

        //then
        result.andExpect(status().isBadRequest());
        assertThat(response.getId()).isEqualTo(Errors.CONVERSATION_NOT_EXISTS.getId());
    }

    @Test
    public void getBeforeMessagesInvalidAndNoAuthHeader() throws Exception {
        testInvalidAndNullToken(get("/messages/before/1/1"));
    }

    @Test
    public void beforeMessages() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(logged);

        ConversationEntity conversationEntity = new ConversationEntity().setName(CONVERSATION_NAME).setUsers(logged);
        conversationDao.persist(conversationEntity);
        List<MessageEntity> newMessages = createMessages(logged, conversationEntity, 50);
        Long lastMsgId = newMessages.get(40).getId();

        //when
        ResultActions result = mock.perform(get("/messages/before/" + conversationEntity.getId() + "/" + lastMsgId).header(AUTH_TOKEN_HEADER, token));
        List<MessageResponse> messages = responseFromJson(result, new TypeReference<List<MessageResponse>>() {
        });

        //then
        result.andExpect(status().isOk());
        assertThat(messages).hasSize(20);
        assertThat(messages.get(0).getMessage()).isEqualTo(MESSAGE + 21);
        assertThat(messages.get(19).getMessage()).isEqualTo(MESSAGE + 40);
    }

    @Test
    public void beforeMessagesConversationNotExists() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(logged);
        Long notExistingConversationId = 123L;

        //when
        ResultActions result = mock.perform(get("/messages/before/" + notExistingConversationId + "/1").header(AUTH_TOKEN_HEADER, token));
        ResponseError response = responseFromJson(result, ResponseError.class);

        //then
        result.andExpect(status().isBadRequest());
        assertThat(response.getId()).isEqualTo(Errors.CONVERSATION_NOT_EXISTS.getId());
    }

    @Test
    public void beforeMessagesNotMyConversation() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        UserEntity user1 = registerUser("user1", EMAIL, PASSWORD);
        String token = userService.login(logged);

        ConversationEntity conversationEntity = new ConversationEntity().setName(CONVERSATION_NAME).setUsers(user1);
        conversationDao.persist(conversationEntity);

        //when
        ResultActions result = mock.perform(get("/messages/before/" + conversationEntity.getId() + "/1").header(AUTH_TOKEN_HEADER, token));
        ResponseError response = responseFromJson(result, ResponseError.class);

        //then
        result.andExpect(status().isBadRequest());
        assertThat(response.getId()).isEqualTo(Errors.CONVERSATION_NOT_EXISTS.getId());
    }

    @Test
    public void beforeMessagesMessageNotExists() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(logged);
        Long notExistingMessageId = 123L;

        ConversationEntity conversationEntity = new ConversationEntity().setName(CONVERSATION_NAME).setUsers(logged);
        conversationDao.persist(conversationEntity);

        //when
        ResultActions result = mock.perform(get("/messages/before/" + conversationEntity.getId() + "/" + notExistingMessageId).header(AUTH_TOKEN_HEADER, token));
        ResponseError response = responseFromJson(result, ResponseError.class);

        //then
        result.andExpect(status().isBadRequest());
        assertThat(response.getId()).isEqualTo(Errors.MESSAGE_NOT_EXISTS.getId());
    }

    @Test
    public void getGroupsWithNewMessagesInvalidAndNoAuthHeader() throws Exception {
        testInvalidAndNullToken(get("/messages/unread"));
    }

    @Test
    public void getGroupsWithNewMessages() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        UserEntity user2 = registerUser("user2", EMAIL, PASSWORD);
        UserEntity user3 = registerUser("user3", EMAIL, PASSWORD);
        String token = userService.login(logged);

        ConversationEntity c1 = new ConversationEntity().setUsers(logged).setName("c1");
        ConversationEntity c2 = new ConversationEntity().setUsers(logged, user2).setName("c2");
        ConversationEntity c3 = new ConversationEntity().setUsers(logged, user3).setName("c3");
        ConversationEntity c4 = new ConversationEntity().setUsers(user2, user3).setName("c4");
        conversationDao.persist(c1);
        conversationDao.persist(c2);
        conversationDao.persist(c3);
        conversationDao.persist(c4);

        List<MessageEntity> newMessages1 = createMessages(logged, c1, 5);
        List<MessageEntity> newMessages2 = createMessages(user2, c2, 5);
        List<MessageEntity> newMessages3 = createMessages(user3, c3, 5);
        List<MessageEntity> newMessages4 = createMessages(user2, c4, 5);

        setLastReadMessage(logged, c1, newMessages1.get(4));
        setLastReadMessage(logged, c3, newMessages2.get(0));

        setLastReadMessage(user2, c2, newMessages2.get(2));
        setLastReadMessage(user3, c3, newMessages3.get(0));
        setLastReadMessage(user2, c4, newMessages4.get(4));

        //when
        ResultActions result = mock.perform(get("/messages/unread").header(AUTH_TOKEN_HEADER, token));
        List<Long> unreadGroups = responseFromJson(result, new TypeReference<List<Long>>() {
        });

        //then
        assertThat(unreadGroups).hasSize(2);
        assertThat(unreadGroups.get(0)).isEqualTo(c2.getId());
        assertThat(unreadGroups.get(1)).isEqualTo(c3.getId());
    }

    @Test
    public void getGroupsWithNewMessagesAllRead() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        UserEntity user2 = registerUser("user2", EMAIL, PASSWORD);
        String token = userService.login(logged);

        ConversationEntity c1 = new ConversationEntity().setUsers(logged).setName("c1");
        ConversationEntity c2 = new ConversationEntity().setUsers(logged, user2).setName("c2");
        conversationDao.persist(c1);
        conversationDao.persist(c2);

        List<MessageEntity> newMessages1 = createMessages(logged, c1, 5);
        List<MessageEntity> newMessages2 = createMessages(user2, c2, 5);

        setLastReadMessage(logged, c1, newMessages1.get(4));
        setLastReadMessage(logged, c2, newMessages2.get(4));

        //when
        ResultActions result = mock.perform(get("/messages/unread").header(AUTH_TOKEN_HEADER, token));
        List<Long> unreadGroups = responseFromJson(result, new TypeReference<List<Long>>() {
        });

        //then
        assertThat(unreadGroups).isEmpty();
    }

    @Test
    public void getGroupsWithNewMessagesNoGroups() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(logged);

        //when
        ResultActions result = mock.perform(get("/messages/unread").header(AUTH_TOKEN_HEADER, token));
        List<Long> unreadGroups = responseFromJson(result, new TypeReference<List<Long>>() {
        });

        //then
        assertThat(unreadGroups).isEmpty();
    }

    private void setLastReadMessage(UserEntity user, ConversationEntity conversation, MessageEntity lastSeenPost) {
        UserConversationEntity userConversation = userConversationDao.findByUserAndConversation(user, conversation);
        userConversation.setLastSeenPost(lastSeenPost);
        userConversationDao.merge(userConversation);
    }

    /**
     * Creates and persists Message entities with generate message = MESSAGE + i; i belong to [0, amount]
     *
     * @return All created entities
     */
    private List<MessageEntity> createMessages(UserEntity userEntity, ConversationEntity conversationEntity, int amount) {
        long data = 1464429746; //28.05.2016
        List<MessageEntity> entities = new LinkedList<>();
        for (int i = 1; i <= amount; i++) {
            MessageEntity messageEntity = new MessageEntity()
                    .setDate(new Timestamp(data + i * 10000))
                    .setUser(userEntity)
                    .setConversation(conversationEntity)
                    .setMessage(MESSAGE + i);
            messageDao.persist(messageEntity);
            entities.add(messageEntity);
        }
        return entities;
    }
}