package net.chat.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Optional;
import net.chat.entity.ConversationEntity;
import net.chat.entity.UserEntity;
import net.chat.repository.ConversationDao;
import net.chat.repository.UserConversationDao;
import net.chat.rest.dto.ConversationResponse;
import net.chat.rest.dto.CreateGroupResponse;
import net.chat.rest.dto.InviteToGroupParams;
import net.chat.rest.dto.RenameGroupParams;
import net.chat.rest.message.Errors;
import net.chat.rest.message.ResponseError;
import net.chat.rest.message.RestResponse;
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
 * @since 13.05.2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {MockTestConfiguration.class}, loader = AnnotationConfigWebContextLoader.class)
@Transactional
public class GroupRestControllerTest extends BaseRestControllerTest {
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "test@test.pl";

    @Autowired
    private GroupRestController objectUnderTest;

    @Autowired
    UserService userService;

    @Autowired
    ConversationDao conversationDao;

    @Autowired
    UserConversationDao userConversationDao;

    @Override
    public GroupRestController getObjectUnderTest() {
        return objectUnderTest;
    }

    @Test
    public void createGroupInvalidAndNoAuthHeader() throws Exception {
        testInvalidAndNullToken(get("/groups/create"));
    }

    @Test
    public void createGroup() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(logged);

        //when
        ResultActions result = mock.perform(get("/groups/create").header(AUTH_TOKEN_HEADER, token));
        CreateGroupResponse response = responseFromJson(result, CreateGroupResponse.class);

        //then
        result.andExpect(status().isOk());
        Optional<ConversationEntity> newConversation = conversationDao.findById(response.getId());
        assertThat(newConversation.isPresent()).isTrue();
        assertThat(newConversation.get().isGroup()).isTrue();
        assertThat(newConversation.get().getUsers().get(0).getName()).isEqualTo(USERNAME);
    }

    @Test
    public void inviteToGroupInvalidAndNoAuthHeader() throws Exception {
        testInvalidAndNullToken(post("/groups/invite"), new InviteToGroupParams());
    }

    @Test
    public void inviteToGroup() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        UserEntity user1 = registerUser("user1", EMAIL, PASSWORD);
        UserEntity user2 = registerUser("user2", EMAIL, PASSWORD);
        String token = userService.login(logged);

        ConversationEntity conversationEntity = new ConversationEntity().setUsers(logged).setGroup(true);
        conversationDao.persist(conversationEntity);

        InviteToGroupParams params = new InviteToGroupParams().setGroupId(conversationEntity.getId()).setUserIds(new Long[]{user1.getId(), user2.getId()});

        //when
        ResultActions result = mock.perform(post("/groups/invite").content(toJson(params)).contentType(MediaType.APPLICATION_JSON).header(AUTH_TOKEN_HEADER, token));
        RestResponse response = responseFromJson(result, RestResponse.class);

        //then
        result.andExpect(status().isOk());
        conversationEntity = conversationDao.findById(conversationEntity.getId()).get();
        assertThat(conversationEntity.getUsers()).contains(logged);
        assertThat(conversationEntity.getUsers()).contains(user1);
        assertThat(conversationEntity.getUsers()).contains(user2);
    }

    @Test
    public void inviteToNotExistingGroup() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        UserEntity user1 = registerUser("user1", EMAIL, PASSWORD);
        String token = userService.login(logged);

        Long notExistingGroupId = 123L;
        InviteToGroupParams params = new InviteToGroupParams().setGroupId(notExistingGroupId).setUserIds(new Long[]{user1.getId()});

        //when
        ResultActions result = mock.perform(post("/groups/invite").content(toJson(params)).contentType(MediaType.APPLICATION_JSON).header(AUTH_TOKEN_HEADER, token));
        ResponseError response = responseFromJson(result, ResponseError.class);

        //then
        result.andExpect(status().isBadRequest());
        assertThat(response.getId()).isEqualTo(Errors.GROUP_NOT_EXISTS.getId());
    }

    @Test
    public void inviteNotExistingUserToGroup() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(logged);

        ConversationEntity conversationEntity = new ConversationEntity().setUsers(logged).setGroup(true);
        conversationDao.persist(conversationEntity);

        Long notExistingUserId = 123L;
        InviteToGroupParams params = new InviteToGroupParams().setGroupId(conversationEntity.getId()).setUserIds(new Long[]{notExistingUserId});

        //when
        ResultActions result = mock.perform(post("/groups/invite").content(toJson(params)).contentType(MediaType.APPLICATION_JSON).header(AUTH_TOKEN_HEADER, token));
        ResponseError response = responseFromJson(result, ResponseError.class);

        //then
        result.andExpect(status().isBadRequest());
        assertThat(response.getId()).isEqualTo(Errors.USER_NOT_EXISTS.getId());
    }

    @Test
    public void inviteToNotMyGroup() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        UserEntity user1 = registerUser("user1", EMAIL, PASSWORD);
        UserEntity user2 = registerUser("user2", EMAIL, PASSWORD);
        String token = userService.login(logged);

        ConversationEntity conversationEntity = new ConversationEntity().setUsers(user1).setGroup(true);
        conversationDao.persist(conversationEntity);

        InviteToGroupParams params = new InviteToGroupParams().setGroupId(conversationEntity.getId()).setUserIds(new Long[]{user1.getId(), user2.getId()});

        //when
        ResultActions result = mock.perform(post("/groups/invite").content(toJson(params)).contentType(MediaType.APPLICATION_JSON).header(AUTH_TOKEN_HEADER, token));
        ResponseError response = responseFromJson(result, ResponseError.class);

        //then
        result.andExpect(status().isBadRequest());
        assertThat(response.getId()).isEqualTo(Errors.GROUP_NOT_EXISTS.getId());
    }

    @Test
    public void inviteToGroupNullCredentials() throws Exception {
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(logged);
        testNullCredentials(post("/groups/invite"), new InviteToGroupParams(), token);
    }

    @Test
    public void renameGroupInvalidAndNoAuthHeader() throws Exception {
        testInvalidAndNullToken(post("/groups/rename"), new RenameGroupParams());
    }

    @Test
    public void renameGroupNullCredentials() throws Exception {
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(logged);
        testNullCredentials(post("/groups/rename"), new RenameGroupParams(), token);
    }

    @Test
    public void renameGroup() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(logged);

        ConversationEntity conversationEntity = new ConversationEntity().setUsers(logged).setGroup(true).setName("oldName");
        conversationDao.persist(conversationEntity);

        RenameGroupParams params = new RenameGroupParams().setGroupId(conversationEntity.getId()).setNewName("newName");

        //when
        ResultActions result = mock.perform(post("/groups/rename").content(toJson(params)).contentType(MediaType.APPLICATION_JSON).header(AUTH_TOKEN_HEADER, token));
        RestResponse response = responseFromJson(result, RestResponse.class);

        //then
        result.andExpect(status().isOk());
        conversationEntity = conversationDao.findById(conversationEntity.getId()).get();
        assertThat(conversationEntity.getName()).isEqualTo("newName");
    }

    @Test
    public void renameNotExistingGroup() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(logged);

        Long notExistingGroupId = 123L;
        RenameGroupParams params = new RenameGroupParams().setGroupId(notExistingGroupId).setNewName("newName");

        //when
        ResultActions result = mock.perform(post("/groups/rename").content(toJson(params)).contentType(MediaType.APPLICATION_JSON).header(AUTH_TOKEN_HEADER, token));
        ResponseError response = responseFromJson(result, ResponseError.class);

        //then
        result.andExpect(status().isBadRequest());
        assertThat(response.getId()).isEqualTo(Errors.GROUP_NOT_EXISTS.getId());
    }

    @Test
    public void renameNotMyGroup() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        UserEntity user1 = registerUser("user1", EMAIL, PASSWORD);
        String token = userService.login(logged);

        ConversationEntity conversationEntity = new ConversationEntity().setUsers(user1).setGroup(true);
        conversationDao.persist(conversationEntity);

        RenameGroupParams params = new RenameGroupParams().setGroupId(conversationEntity.getId()).setNewName("newName");

        //when
        ResultActions result = mock.perform(post("/groups/rename").content(toJson(params)).contentType(MediaType.APPLICATION_JSON).header(AUTH_TOKEN_HEADER, token));
        ResponseError response = responseFromJson(result, ResponseError.class);

        //then
        result.andExpect(status().isBadRequest());
        assertThat(response.getId()).isEqualTo(Errors.GROUP_NOT_EXISTS.getId());
    }

    @Test
    public void myGroupsInvalidAndNoAuthHeader() throws Exception {
        testInvalidAndNullToken(get("/groups/my"));
    }

    @Test
    public void myGroups() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        UserEntity user1 = registerUser("user1", EMAIL, PASSWORD);
        UserEntity user2 = registerUser("user2", EMAIL, PASSWORD);
        String token = userService.login(logged);

        ConversationEntity c1 = new ConversationEntity().setUsers(logged).setGroup(true).setName("c1");
        ConversationEntity c2 = new ConversationEntity().setUsers(logged, user1).setGroup(false).setName("c2");
        ConversationEntity c3 = new ConversationEntity().setUsers(logged, user2, user1).setGroup(true).setName("c3");
        ConversationEntity c4 = new ConversationEntity().setUsers(user2, user1).setGroup(true).setName("c4");
        conversationDao.persist(c1);
        conversationDao.persist(c2);
        conversationDao.persist(c3);
        conversationDao.persist(c4);

        //when
        ResultActions result = mock.perform(get("/groups/my").header(AUTH_TOKEN_HEADER, token));
        List<ConversationResponse> conversations = responseFromJson(result, new TypeReference<List<ConversationResponse>>() {
        });

        //then
        result.andExpect(status().isOk());
        assertThat(conversations).hasSize(2);
        assertThat(conversations.get(0).getName()).isEqualTo("c1");
        assertThat(conversations.get(1).getName()).isEqualTo("c3");
    }
}