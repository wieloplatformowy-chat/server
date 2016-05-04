package net.chat.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import net.chat.config.authentication.TokenService;
import net.chat.entity.UserEntity;
import net.chat.repository.FriendDao;
import net.chat.rest.dto.IdDto;
import net.chat.rest.dto.UserWithoutPasswordDto;
import net.chat.rest.message.Errors;
import net.chat.rest.message.ResponseError;
import net.chat.service.FriendService;
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

import static net.chat.rest.dto.UserWithoutPasswordDto.fromEntity;
import static org.fest.assertions.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Mariusz Gorzycki
 * @since 03.05.2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {MockTestConfiguration.class}, loader = AnnotationConfigWebContextLoader.class)
@Transactional
public class FriendRestControllerTest extends BaseRestControllerTest {
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "test@test.pl";
    public static final String FRIEND_NAME = "friendName";

    @Autowired
    private FriendRestController objectUnderTest;

    @Autowired
    TokenService tokenService;

    @Autowired
    FriendService friendService;

    @Autowired
    FriendDao friendDao;

    @Autowired
    UserService userService;

    @Override
    public FriendRestController getObjectUnderTest() {
        return objectUnderTest;
    }

    @Test
    public void addFriendNullCredentials() throws Exception {
        testNullCredentials(post("/friends/add"), new IdDto().setId(null));
    }

    @Test
    public void addFriendInvalidAndNoAuthHeader() throws Exception {
        testInvalidAndNullToken(post("/friends/add"), new IdDto().setId(1L));
    }

    @Test
    public void addFriendSuccess() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(logged);
        UserEntity user = registerUser(FRIEND_NAME, EMAIL, PASSWORD);
        IdDto idDto = new IdDto().setId(user.getId());

        //when
        ResultActions result = mock.perform(post("/friends/add").content(toJson(idDto)).contentType(MediaType.APPLICATION_JSON).header(AUTH_TOKEN_HEADER, token));

        //then
        result.andExpect(status().isOk());
        assertThat(friendService.isMyFriend(user)).isTrue();
    }

    @Test
    public void addFriendAlreadyAFriend() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(logged);
        UserEntity user = registerUser(FRIEND_NAME, EMAIL, PASSWORD);
        IdDto idDto = new IdDto().setId(user.getId());

        //when
        mock.perform(post("/friends/add").content(toJson(idDto)).contentType(MediaType.APPLICATION_JSON).header(AUTH_TOKEN_HEADER, token));
        ResultActions result = mock.perform(post("/friends/add").content(toJson(idDto)).contentType(MediaType.APPLICATION_JSON).header(AUTH_TOKEN_HEADER, token));
        ResponseError response = responseFromJson(result, ResponseError.class);

        //then
        result.andExpect(status().isBadRequest());
        assertThat(response.getId()).isEqualTo(Errors.ALREADY_A_FRIEND.getId());
    }

    @Test()
    public void searchInvalidAndNoAuthHeader() throws Exception {
        testInvalidAndNullToken(get("/friends/my"));
    }

    @Test()
    public void searchSuccess() throws Exception {
        //given
        UserEntity friend1 = registerUser("Friend1", EMAIL, PASSWORD);
        UserEntity friend2 = registerUser("Friend2", EMAIL, PASSWORD);
        UserEntity firstFriend = registerUser("AAFriend4", EMAIL, PASSWORD);
        UserEntity notFriend = registerUser("Friend5", EMAIL, PASSWORD);
        UserEntity notMyFriend = registerUser("Friend6", EMAIL, PASSWORD);
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(logged);
        friendDao.addFriend(logged, friend1);
        friendDao.addFriend(logged, friend2);
        friendDao.addFriend(logged, firstFriend);
        friendDao.addFriend(friend1, notMyFriend);
        friendDao.addFriend(friend1, friend2);

        //when
        ResultActions result = mock.perform(get("/friends/my").header(AUTH_TOKEN_HEADER, token));
        List<UserWithoutPasswordDto> list = responseFromJson(result, new TypeReference<List<UserWithoutPasswordDto>>() {
        });

        //then
        result.andExpect(status().isOk());
        assertThat(list.contains(fromEntity(friend1))).isTrue();
        assertThat(list.contains(fromEntity(friend2))).isTrue();
        assertThat(list.get(0).equals(fromEntity(firstFriend))).isTrue();
        assertThat(list.contains(fromEntity(notFriend))).isFalse();
        assertThat(list.contains(fromEntity(notMyFriend))).isFalse();
        assertThat(list.contains(fromEntity(logged))).isFalse();
    }

    @Test()
    public void searchSuccessEmpty() throws Exception {
        //given
        UserEntity notMyFriend1 = registerUser("Friend1", EMAIL, PASSWORD);
        UserEntity notMyFriend2 = registerUser("Friend2", EMAIL, PASSWORD);
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(logged);
        friendDao.addFriend(notMyFriend1, notMyFriend2);
        friendDao.addFriend(notMyFriend2, notMyFriend1);

        //when
        ResultActions result = mock.perform(get("/friends/my").header(AUTH_TOKEN_HEADER, token));
        List<UserWithoutPasswordDto> list = responseFromJson(result, new TypeReference<List<UserWithoutPasswordDto>>() {
        });

        //then
        result.andExpect(status().isOk());
        assertThat(list.isEmpty()).isTrue();
    }
}