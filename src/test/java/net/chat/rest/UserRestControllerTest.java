package net.chat.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import net.chat.config.authentication.TokenService;
import net.chat.entity.UserEntity;
import net.chat.exception.LoginRequiredException;
import net.chat.rest.dto.*;
import net.chat.rest.message.Errors;
import net.chat.rest.message.ResponseError;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static net.chat.rest.dto.UserWithoutPasswordDto.fromEntity;
import static org.fest.assertions.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Mariusz Gorzycki
 * @since 30.04.2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {MockTestConfiguration.class}, loader = AnnotationConfigWebContextLoader.class)
@Transactional
public class UserRestControllerTest extends BaseRestControllerTest {
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "test@test.pl";

    @Autowired
    private UserRestController objectUnderTest;

    @Autowired
    TokenService tokenService;

    @Override
    public Object getObjectUnderTest() {
        return objectUnderTest;
    }

    @Test
    public void registerSuccess() throws Exception {
        //given
        UserDto userDto = new UserDto().setName(USERNAME).setEmail(EMAIL).setPassword(PASSWORD);

        //when
        ResultActions result = mock.perform(post("/user/register").content(toJson(userDto)).contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk());
    }

    @Test
    public void registerUserAlreadyExists() throws Exception {
        //given
        UserDto userDto = new UserDto().setName(USERNAME).setEmail(EMAIL).setPassword(PASSWORD);

        //when
        mock.perform(post("/user/register").content(toJson(userDto)).contentType(MediaType.APPLICATION_JSON));
        ResultActions result = mock.perform(post("/user/register").content(toJson(userDto)).contentType(MediaType.APPLICATION_JSON));
        ResponseError response = responseFromJson(result, ResponseError.class);

        //then
        result.andExpect(status().isBadRequest());
        assertThat(response.getId()).isEqualTo(Errors.USERNAME_IS_TAKEN.getId());
    }

    @Test
    public void registerNullCredentials() throws Exception {
        testNullCredentials(post("/user/register"), new UserDto().setName(null).setEmail(EMAIL).setPassword(PASSWORD));
    }

    @Test
    public void loginSuccess() throws Exception {
        //given
        UserDto userDto = new UserDto().setName(USERNAME).setEmail(EMAIL).setPassword(PASSWORD);
        userService.register(userDto.toUserWithNullId());
        LoginDto loginDto = LoginDto.fromUserDto(userDto);

        //when
        ResultActions result = mock.perform(post("/user/login").content(toJson(loginDto)).contentType(MediaType.APPLICATION_JSON));
        TokenDto tokenDto = responseFromJson(result, TokenDto.class);

        //then
        result.andExpect(status().isOk());
        assertThat(UUID.fromString(tokenDto.getToken())).isNotNull();
    }

    @Test
    public void loginUserNotExists() throws Exception {
        //given
        UserDto userDto = new UserDto().setName(USERNAME).setEmail(EMAIL).setPassword(PASSWORD);
        LoginDto loginDto = LoginDto.fromUserDto(userDto);

        //when
        ResultActions result = mock.perform(post("/user/login").content(toJson(loginDto)).contentType(MediaType.APPLICATION_JSON));
        ResponseError response = responseFromJson(result, ResponseError.class);

        //then
        result.andExpect(status().isBadRequest());
        assertThat(response.getId()).isEqualTo(Errors.USER_NOT_EXISTS.getId());
    }

    @Test
    public void loginInvalidPassword() throws Exception {
        //given
        registerUser(USERNAME, EMAIL, PASSWORD);
        LoginDto loginDtoInvalidPassword = new LoginDto().setName(USERNAME).setPassword("invalidPassword");

        //when
        ResultActions result = mock.perform(post("/user/login").content(toJson(loginDtoInvalidPassword)).contentType(MediaType.APPLICATION_JSON));
        ResponseError response = responseFromJson(result, ResponseError.class);

        //then
        result.andExpect(status().isBadRequest());
        assertThat(response.getId()).isEqualTo(Errors.INVALID_PASSWORD.getId());
    }

    @Test
    public void loginNullCredentials() throws Exception {
        testNullCredentials(post("/user/login"), new LoginDto().setName(null).setPassword(PASSWORD));
    }

    @Test
    public void deleteSuccess() throws Exception {
        //given
        UserEntity userEntity = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(userEntity);
        PasswordDto password = PasswordDto.with(PASSWORD);

        //when
        ResultActions result = mock.perform(delete("/user/delete").content(toJson(password)).contentType(MediaType.APPLICATION_JSON).header(AUTH_TOKEN_HEADER, token));

        //then
        result.andExpect(status().isOk());
        assertThat(userService.isUserNameTaken(USERNAME)).isFalse();
    }

    @Test
    public void deleteInvalidAndNoAuthHeader() throws Exception {
        //given
        UserEntity userEntity = registerUser(USERNAME, EMAIL, PASSWORD);
        userService.login(userEntity);
        PasswordDto password = PasswordDto.with(PASSWORD);

        //when
        testInvalidAndNullToken(delete("/user/delete"), password);

        //then
        assertThat(userService.isUserNameTaken(USERNAME)).isTrue();
    }

    @Test
    public void logoutInvalidAndNoAuthHeader() throws Exception {
        testInvalidAndNullToken(get("/user/logout"), new SearchUserDto().setName("name").setEmail("required@test.com"));
    }

    @Test
    public void deleteInvalidPassword() throws Exception {
        //given
        UserEntity userEntity = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(userEntity);
        PasswordDto password = PasswordDto.with("invalidPassword");

        //when
        ResultActions result = mock.perform(delete("/user/delete").content(toJson(password)).contentType(MediaType.APPLICATION_JSON).header(AUTH_TOKEN_HEADER, token));
        ResponseError response = responseFromJson(result, ResponseError.class);

        //then
        result.andExpect(status().isBadRequest());
        assertThat(response.getId()).isEqualTo(Errors.INVALID_PASSWORD.getId());
        assertThat(userService.isUserNameTaken(USERNAME)).isTrue();
    }

    @Test
    public void deleteNullPassword() throws Exception {
        //given
        UserEntity userEntity = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(userEntity);
        PasswordDto password = PasswordDto.with(null);
        //when then
        testNullCredentials(delete("/user/delete"), password, token);
        assertThat(userService.isUserNameTaken(USERNAME)).isTrue();
    }

    @Test
    public void whoAmIInvalidAndNoAuthHeader() throws Exception {
        //when
        testInvalidAndNullToken(get("/user/whoami"));
    }

    @Test
    public void whoAmISuccess() throws Exception {
        //given
        UserEntity userEntity = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(userEntity);

        //when
        ResultActions result = mock.perform(get("/user/whoami").header(AUTH_TOKEN_HEADER, token));
        UserWithoutPasswordDto user = responseFromJson(result, UserWithoutPasswordDto.class);

        //then
        result.andExpect(status().isOk());
        assertThat(user.name).isEqualTo(USERNAME);
        assertThat(user.email).isEqualTo(EMAIL);
    }

    @Test(expected = LoginRequiredException.class)
    public void logoutSuccess() throws Exception {
        //given
        UserEntity userEntity = new UserEntity().setName(USERNAME).setEmail(EMAIL).setPassword(PASSWORD);
        userService.register(userEntity);
        String token = userService.login(userEntity);

        //when
        ResultActions result = mock.perform(get("/user/logout").header(AUTH_TOKEN_HEADER, token));

        //then
        result.andExpect(status().isOk());
        assertThat(tokenService.contains(token)).isFalse();
        userService.getLoggedUser(); //throws LoginRequiredException
    }

    @Test()
    public void searchInvalidAndNoAuthHeader() throws Exception {
        testInvalidAndNullToken(post("/user/search"), new SearchUserDto().setName("name").setEmail("required@test.com"));
    }

    @Test()
    public void searchSuccess() throws Exception {
        //given
        UserEntity user1 = registerUser("Username", "email", PASSWORD);
        UserEntity user2 = registerUser("UserNameTwo", "otherMail", PASSWORD);
        UserEntity user3 = registerUser("nAME", "otherMail", PASSWORD);
        UserEntity firstUser = registerUser("AAFirstUser", "required@test.com", PASSWORD);
        UserEntity notMatchingUSer = registerUser("NoMatchMeame", "na@me.com", PASSWORD);
        UserEntity logged = registerUser("MeName", "required@test.com", PASSWORD);
        String token = userService.login(logged);

        SearchUserDto search = new SearchUserDto().setName("name").setEmail("required@test.com");

        //when
        ResultActions result = mock.perform(post("/user/search").content(toJson(search)).contentType(MediaType.APPLICATION_JSON).header(AUTH_TOKEN_HEADER, token));
        List<UserWithoutPasswordDto> list = responseFromJson(result, new TypeReference<List<UserWithoutPasswordDto>>() {
        });

        //then
        result.andExpect(status().isOk());
        assertThat(list.contains(fromEntity(user1))).isTrue();
        assertThat(list.contains(fromEntity(user2))).isTrue();
        assertThat(list.contains(fromEntity(user3))).isTrue();
        assertThat(list.get(0).equals(fromEntity(firstUser))).isTrue();
        assertThat(list.contains(fromEntity(notMatchingUSer))).isFalse();
        assertThat(list.contains(fromEntity(logged))).isFalse();
    }

    @Test()
    public void searchSuccessNullName() throws Exception {
        //given
        UserEntity notFound = registerUser("Username", "email", PASSWORD);
        UserEntity user = registerUser("AAFirstUser", "required@test.com", PASSWORD);
        UserEntity logged = registerUser("MeName", "required@test.com", PASSWORD);
        String token = userService.login(logged);

        SearchUserDto search = new SearchUserDto().setName(null).setEmail("test.com");

        //when
        ResultActions result = mock.perform(post("/user/search").content(toJson(search)).contentType(MediaType.APPLICATION_JSON).header(AUTH_TOKEN_HEADER, token));
        List<UserWithoutPasswordDto> list = responseFromJson(result, new TypeReference<List<UserWithoutPasswordDto>>() {
        });

        //then
        result.andExpect(status().isOk());
        assertThat(list.contains(fromEntity(user))).isTrue();
        assertThat(list.contains(fromEntity(logged))).isFalse();
        assertThat(list.contains(fromEntity(notFound))).isFalse();
    }

    @Test()
    public void searchSuccessNullEmail() throws Exception {
        //given
        UserEntity notFound = registerUser("Username", "email", PASSWORD);
        UserEntity user = registerUser("AAFirstUser", "first", PASSWORD);
        UserEntity logged = registerUser("MeName", "required@test.com", PASSWORD);
        String token = userService.login(logged);

        SearchUserDto search = new SearchUserDto().setName("firST").setEmail(null);

        //when
        ResultActions result = mock.perform(post("/user/search").content(toJson(search)).contentType(MediaType.APPLICATION_JSON).header(AUTH_TOKEN_HEADER, token));
        List<UserWithoutPasswordDto> list = responseFromJson(result, new TypeReference<List<UserWithoutPasswordDto>>() {
        });

        //then
        result.andExpect(status().isOk());
        assertThat(list.contains(fromEntity(user))).isTrue();
        assertThat(list.contains(fromEntity(logged))).isFalse();
        assertThat(list.contains(fromEntity(notFound))).isFalse();
    }

    @Test()
    public void searchSuccessEmpty() throws Exception {
        //given
        registerUser("AAFirstUser", "first", PASSWORD);
        UserEntity logged = registerUser("MeName", "required@test.com", PASSWORD);
        String token = userService.login(logged);

        SearchUserDto search = new SearchUserDto().setName(null).setEmail(null);

        //when
        ResultActions result = mock.perform(post("/user/search").content(toJson(search)).contentType(MediaType.APPLICATION_JSON).header(AUTH_TOKEN_HEADER, token));
        List<UserWithoutPasswordDto> list = responseFromJson(result, new TypeReference<List<UserWithoutPasswordDto>>() {
        });

        //then
        result.andExpect(status().isOk());
        assertThat(list.isEmpty()).isTrue();
    }

    @Test
    public void invalidJson() throws Exception {
        UserEntity userEntity = registerUser(USERNAME, EMAIL, PASSWORD);
        String token = userService.login(userEntity);

        testInvalidJson(MockMvcRequestBuilders.post("/user/register"), token);
        testInvalidJson(post("/user/login"), token);
        testInvalidJson(delete("/user/delete"), token);
        testInvalidJson(post("/user/search"), token);
    }
}