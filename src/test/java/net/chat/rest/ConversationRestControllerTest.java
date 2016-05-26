package net.chat.rest;

import net.chat.entity.ConversationEntity;
import net.chat.entity.UserEntity;
import net.chat.repository.ConversationDao;
import net.chat.repository.UserConversationDao;
import net.chat.rest.dto.ConversationResponse;
import net.chat.rest.message.Errors;
import net.chat.rest.message.ResponseError;
import net.chat.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.fest.assertions.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Mariusz Gorzycki
 * @since 13.05.2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {MockTestConfiguration.class}, loader = AnnotationConfigWebContextLoader.class)
@Transactional
public class ConversationRestControllerTest extends BaseRestControllerTest {
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "test@test.pl";
    private static final String CONVERSATION_NAME = "ConversationName";

    @Autowired
    private ConversationRestController objectUnderTest;

    @Autowired
    UserService userService;

    @Autowired
    ConversationDao conversationDao;

    @Autowired
    UserConversationDao userConversationDao;

    @Override
    public ConversationRestController getObjectUnderTest() {
        return objectUnderTest;
    }

    @Test
    public void getConversationInvalidAndNoAuthHeader() throws Exception {
        testInvalidAndNullToken(get("/conversations/1"));
    }

    @Test
    public void createConversationWithMysellf() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(logged);
        Long id = logged.getId();

        //when
        ResultActions result = mock.perform(get("/conversations/" + id).header(AUTH_TOKEN_HEADER, token));
        ConversationResponse conversation = responseFromJson(result, ConversationResponse.class);

        //then
        result.andExpect(status().isOk());
        assertThat(conversationDao.findAll()).hasSize(1);
        assertThat(userConversationDao.findAll()).hasSize(1);
        assertThat(conversation.getUsers().get(0).name).isEqualTo(USERNAME);
    }

    @Test
    public void getConversationWithMysellf() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        ConversationEntity conversationEntity = new ConversationEntity().setName(CONVERSATION_NAME).setUsers(logged);
        conversationDao.persist(conversationEntity);
        String token = userService.login(logged);
        Long id = logged.getId();

        //when
        ResultActions result = mock.perform(get("/conversations/" + id).header(AUTH_TOKEN_HEADER, token));
        ConversationResponse conversation = responseFromJson(result, ConversationResponse.class);

        //then
        result.andExpect(status().isOk());
        assertThat(conversation.getName()).isEqualTo(CONVERSATION_NAME);
        assertThat(conversation.getUsers().get(0).name).isEqualTo(USERNAME);
    }

    @Test
    public void createConversation() throws Exception {
        //given
        UserEntity logged = registerUser("me", EMAIL, PASSWORD);
        UserEntity interlocutor = registerUser("interlocutor", EMAIL, PASSWORD);
        String token = userService.login(logged);
        Long id = interlocutor.getId();

        //when
        ResultActions result = mock.perform(get("/conversations/" + id).header(AUTH_TOKEN_HEADER, token));
        ConversationResponse conversation = responseFromJson(result, ConversationResponse.class);

        //then
        result.andExpect(status().isOk());
        assertThat(conversationDao.findAll()).hasSize(1);
        assertThat(userConversationDao.findAll()).hasSize(2);
        assertThat(conversation.getUsers().get(0).name).isEqualTo("me");
        assertThat(conversation.getUsers().get(1).name).isEqualTo("interlocutor");
    }

    @Test
    public void createConversationUserNotExists() throws Exception {
        //given
        UserEntity logged = registerUser("me", EMAIL, PASSWORD);
        String token = userService.login(logged);
        Long notExistingId = 123L;

        //when
        ResultActions result = mock.perform(get("/conversations/" + notExistingId).header(AUTH_TOKEN_HEADER, token));
        ResponseError response = responseFromJson(result, ResponseError.class);

        //then
        result.andExpect(status().isBadRequest());
        assertThat(response.getId()).isEqualTo(Errors.USER_NOT_EXISTS.getId());
    }
}