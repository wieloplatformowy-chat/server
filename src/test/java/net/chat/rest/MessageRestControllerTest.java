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
        assertThat(message.getMessaage()).isEqualTo(MESSAGE);
        assertThat(message.getUser()).isEqualTo(logged);
        assertThat(message.getConversation()).isEqualTo(conversationEntity);
        assertThat(message.getDate()).isNotNull();

        UserConversationEntity userConversation = userConversationDao.findByUserAndConversation(logged, conversationEntity);
        assertThat(userConversation.getLastSeenPostId()).isEqualTo(message.getId());
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
        assertThat(response.getId()).isEqualTo(Errors.GROUP_NOT_EXISTS.getId());
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
        assertThat(response.getId()).isEqualTo(Errors.GROUP_NOT_EXISTS.getId());
    }

    @Test
    public void getLastMessagesInvalidAndNoAuthHeader() throws Exception {
        testInvalidAndNullToken(get("/messages/last/1"), new SendMessageParams());
    }

    @Test
    public void lastMessages() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(logged);

        ConversationEntity conversationEntity = new ConversationEntity().setName(CONVERSATION_NAME).setUsers(logged);
        conversationDao.persist(conversationEntity);
        create25Messages(logged, conversationEntity);

        SendMessageParams params = new SendMessageParams().setConversationId(conversationEntity.getId()).setMessage(MESSAGE);

        //when
        ResultActions result = mock.perform(get("/messages/last/" + conversationEntity.getId()).content(toJson(params)).contentType(MediaType.APPLICATION_JSON).header(AUTH_TOKEN_HEADER, token));
        List<MessageResponse> messages = responseFromJson(result, new TypeReference<List<MessageResponse>>() {
        });

        //then
        result.andExpect(status().isOk());
        assertThat(messages).hasSize(20);
        assertThat(messages.get(0).getMessaage()).isEqualTo(MESSAGE + 4);
        assertThat(messages.get(19).getMessaage()).isEqualTo(MESSAGE + 24);

        UserConversationEntity userConversation = userConversationDao.findByUserAndConversation(logged, conversationEntity);
//        assertThat(userConversation.getLastSeenPostId()).isEqualTo(messaage.getId());
    }

    private void create25Messages(UserEntity userEntity, ConversationEntity conversationEntity) {
        for (int i = 0; i < 25; i++) {
            MessageEntity messageEntity = MessageEntity.withCurrentDate()
                    .setUser(userEntity)
                    .setConversation(conversationEntity)
                    .setMessaage(MESSAGE + i);
            messageDao.persist(messageEntity);
        }
    }

    //    @Test
    public void addFriendUserNotExists() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(logged);
        Long notExistingId = 123L;

        //when
        ResultActions result = mock.perform(post("/friends/add/" + notExistingId).header(AUTH_TOKEN_HEADER, token));
        ResponseError response = responseFromJson(result, ResponseError.class);

        //then
        result.andExpect(status().isBadRequest());
        assertThat(response.getId()).isEqualTo(Errors.USER_NOT_EXISTS.getId());
    }
}