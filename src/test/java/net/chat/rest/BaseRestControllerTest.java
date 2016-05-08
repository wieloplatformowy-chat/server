package net.chat.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chat.config.authentication.AuthenticationFilter;
import net.chat.entity.UserEntity;
import net.chat.rest.message.Errors;
import net.chat.rest.message.ResponseError;
import net.chat.service.UserService;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;

import static org.fest.assertions.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Mariusz Gorzycki
 * @since 04.05.2016
 */
public abstract class BaseRestControllerTest {
    protected static final String AUTH_TOKEN_HEADER = "X-Auth-Token";
    protected static ObjectMapper mapper = new ObjectMapper();

    protected MockMvc mock;

    @Autowired
    protected UserService userService;

    @Autowired
    private AuthenticationManager authentication;

    @PostConstruct
    public void postConstruct() {
        mock = MockMvcBuilders.standaloneSetup(getObjectUnderTest()).addFilter(new AuthenticationFilter(authentication)).build();
    }

    @Before
    public void beforeTest() {
        SecurityContextHolder.clearContext();
    }

    public abstract Object getObjectUnderTest();

    protected UserEntity registerUser(String name, String email, String password) {
        UserEntity userEntity = new UserEntity().setName(name).setEmail(email).setPassword(password);
        userService.register(userEntity);
        return userEntity;
    }

    protected void testInvalidJson(MockHttpServletRequestBuilder request, String token) throws Exception {
        //given
        String invalidJson = "invalidJson{}";

        //when
        ResultActions result = mock.perform(request.content(invalidJson).contentType(MediaType.APPLICATION_JSON).header(AUTH_TOKEN_HEADER, token));
        ResponseError response = responseFromJson(result, ResponseError.class);

        //then
        result.andExpect(status().isBadRequest());
        assertThat(response.getId()).isEqualTo(Errors.INVALID_JSON.getId());
    }

    protected void testNullCredentials(MockHttpServletRequestBuilder request) throws Exception {
        testNullCredentials(request, null, null);
    }

    protected void testNullCredentials(MockHttpServletRequestBuilder request, Object content) throws Exception {
        testNullCredentials(request, content, null);
    }

    protected void testNullCredentials(MockHttpServletRequestBuilder request, Object content, String token) throws Exception {
        //when
        if (content != null)
            request.content(toJson(content)).contentType(MediaType.APPLICATION_JSON);
        if (token != null)
            request.header(AUTH_TOKEN_HEADER, token);

        ResultActions result = mock.perform(request);
        ResponseError response = responseFromJson(result, ResponseError.class);

        //then
        result.andExpect(status().isBadRequest());
        assertThat(response.getId()).isEqualTo(Errors.CREDENTIALS_NOT_PROVIDED.getId());
    }

    protected void testInvalidAndNullToken(MockHttpServletRequestBuilder request) throws Exception {
        testInvalidAndNullToken(request, null);
    }

    protected void testInvalidAndNullToken(MockHttpServletRequestBuilder request, Object content) throws Exception {
        testInvalidAndNullToken(request, content, null);
        testInvalidAndNullToken(request, content, "invalidToken");
    }

    private void testInvalidAndNullToken(MockHttpServletRequestBuilder request, Object content, String token) throws Exception {
        //given
        if (content != null)
            request.content(toJson(content)).contentType(MediaType.APPLICATION_JSON);
        if (token != null)
            request.header(AUTH_TOKEN_HEADER, token);

        //when
        ResultActions result = mock.perform(request);
        ResponseError response = responseFromJson(result, ResponseError.class);

        //then
        result.andExpect(status().isUnauthorized());
        assertThat(response.getId()).isEqualTo(Errors.LOGIN_REQUIRED.getId());
    }

    protected String toJson(Object object) throws java.io.IOException {
        return mapper.writeValueAsString(object);
    }

    protected <T> T responseFromJson(ResultActions result, Class<T> clazz) throws java.io.IOException {
        String jsonInString = result.andReturn().getResponse().getContentAsString();
        return mapper.readValue(jsonInString, clazz);
    }

    protected <T> T responseFromJson(ResultActions result, TypeReference type) throws java.io.IOException {
        String jsonInString = result.andReturn().getResponse().getContentAsString();
        return mapper.readValue(jsonInString, type);
    }
}
