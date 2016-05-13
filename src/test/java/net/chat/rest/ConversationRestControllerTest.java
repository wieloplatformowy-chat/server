package net.chat.rest;

import net.chat.config.authentication.TokenService;
import net.chat.entity.UserEntity;
import net.chat.repository.FriendDao;
import net.chat.repository.GroupDao;
import net.chat.repository.UserDao;
import net.chat.rest.message.Errors;
import net.chat.rest.message.ResponseError;
import net.chat.service.FriendService;
import net.chat.service.UserService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.fest.assertions.Assertions.assertThat;
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
public class ConversationRestControllerTest extends BaseRestControllerTest {
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

    @Autowired
    UserDao userDao;

    @Autowired
    GroupDao groupDao;

    @Override
    public FriendRestController getObjectUnderTest() {
        return objectUnderTest;
    }

    //    @Test
    public void addFriendInvalidAndNoAuthHeader() throws Exception {
        testInvalidAndNullToken(post("/friends/add/1"));
    }

    //    @Test
    public void addFriendSuccess() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(logged);
        UserEntity user = registerUser(FRIEND_NAME, EMAIL, PASSWORD);
        Long id = user.getId();

        //when
        ResultActions result = mock.perform(post("/friends/add/" + id).header(AUTH_TOKEN_HEADER, token));

        //then
        result.andExpect(status().isOk());
        assertThat(friendService.isMyFriend(user)).isTrue();
    }

    //    @Test
    public void addFriendAlreadyAFriend() throws Exception {
        //given
        UserEntity logged = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(logged);
        UserEntity user = registerUser(FRIEND_NAME, EMAIL, PASSWORD);
        friendDao.addFriend(logged, user);
        Long id = user.getId();

        //when
        ResultActions result = mock.perform(post("/friends/add/" + id).header(AUTH_TOKEN_HEADER, token));
        ResponseError response = responseFromJson(result, ResponseError.class);

        //then
        result.andExpect(status().isBadRequest());
        assertThat(response.getId()).isEqualTo(Errors.ALREADY_A_FRIEND.getId());
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