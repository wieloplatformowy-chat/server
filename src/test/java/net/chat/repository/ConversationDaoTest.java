package net.chat.repository;

import net.chat.config.authentication.TokenService;
import net.chat.entity.ConversationEntity;
import net.chat.entity.UserEntity;
import net.chat.rest.MockTestConfiguration;
import net.chat.service.FriendService;
import net.chat.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Mariusz Gorzycki
 * @since 13.05.2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {MockTestConfiguration.class}, loader = AnnotationConfigWebContextLoader.class)
@Transactional
public class ConversationDaoTest {
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "test@test.pl";
    private static final String CONVERSATION_NAME = "ConversationName";

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
    ConversationDao conversationDao;

    @Autowired
    UserConversationDao userConversationDao;

    @Test
    public void testAddConversationsForUser() throws Exception {
        //given
        UserEntity newUser = registerUser(USERNAME, EMAIL, PASSWORD);
        ConversationEntity conversation1 = new ConversationEntity().setName("name");
        ConversationEntity conversation2 = new ConversationEntity().setName("name2");
        newUser.setConversations(conversation1, conversation2);
        userDao.persist(newUser);

        //then
        UserEntity loaded = userDao.findById(newUser.getId());
        assertThat(loaded.getConversations().get(0).getName()).isEqualTo("name");
        assertThat(loaded.getConversations().get(1).getName()).isEqualTo("name2");
        assertThat(userConversationDao.findAll()).hasSize(2);
    }

    @Test
    public void testAddUsersToConversation() throws Exception {
        //given
        UserEntity user1 = registerUser("user1", EMAIL, PASSWORD);
        UserEntity user2 = registerUser("user2", EMAIL, PASSWORD);

        //when
        ConversationEntity conversation = new ConversationEntity().setName(CONVERSATION_NAME);
        conversation.setUsers(user1, user2);
        conversationDao.persist(conversation);

        //then
//        ConversationEntity loaded = conversationDao.findById(conversation.getId());
//        assertThat(loaded.getUsers().get(0).getName()).isEqualTo("user1");
//        assertThat(loaded.getUsers().get(1).getName()).isEqualTo("user2");
//        assertThat(userConversationDao.findAll()).hasSize(2);
    }

    protected UserEntity registerUser(String name, String email, String password) {
        UserEntity userEntity = new UserEntity().setName(name).setEmail(email).setPassword(password);
        userService.register(userEntity);
        return userEntity;
    }
}