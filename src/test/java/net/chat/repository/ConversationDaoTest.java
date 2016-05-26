package net.chat.repository;

import com.google.common.base.Optional;
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

import java.util.List;

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
        ConversationEntity loaded = conversationDao.findById(conversation.getId()).get();
        assertThat(loaded.getUsers().get(0).getName()).isEqualTo("user1");
        assertThat(loaded.getUsers().get(1).getName()).isEqualTo("user2");
        assertThat(userConversationDao.findAll()).hasSize(2);
    }

    @Test
    public void testGetConversationWithMyself() throws Exception {
        //given
        UserEntity user1 = registerUser("user1", EMAIL, PASSWORD);
        UserEntity user2 = registerUser("user2", EMAIL, PASSWORD);
        UserEntity user3 = registerUser("user3", EMAIL, PASSWORD);

        ConversationEntity c1 = new ConversationEntity().setUsers(user1).setName("c1");
        ConversationEntity c2 = new ConversationEntity().setUsers(user1, user2).setName("c2");
        ConversationEntity c3 = new ConversationEntity().setUsers(user1, user3).setName("c3");
        ConversationEntity c4 = new ConversationEntity().setUsers(user2, user3).setName("c4");
        ConversationEntity c5 = new ConversationEntity().setUsers(user3).setName("c5");
        conversationDao.persist(c1);
        conversationDao.persist(c2);
        conversationDao.persist(c3);
        conversationDao.persist(c4);
        conversationDao.persist(c5);

        //when
        Optional<ConversationEntity> conversation = conversationDao.getConversationWithMyself(user1);
        Optional<ConversationEntity> conversation2 = conversationDao.getConversationWithMyself(user3);

        //then
        assertThat(conversation.get().getName()).isEqualTo("c1");
        assertThat(conversation2.get().getName()).isEqualTo("c5");
    }

    @Test
    public void testConversationWithMyselfNotExists() throws Exception {
        //given
        UserEntity user = registerUser(USERNAME, EMAIL, PASSWORD);

        //when
        Optional<ConversationEntity> conversation = conversationDao.getConversationWithMyself(user);

        //then
        assertThat(conversation.isPresent()).isFalse();
    }

    @Test
    public void testGetConversationWith() throws Exception {
        //given
        UserEntity user1 = registerUser("user1", EMAIL, PASSWORD);
        UserEntity user2 = registerUser("user2", EMAIL, PASSWORD);
        UserEntity user3 = registerUser("user3", EMAIL, PASSWORD);

        ConversationEntity c1 = new ConversationEntity().setUsers(user1).setName("c1");
        ConversationEntity c2 = new ConversationEntity().setUsers(user1, user2).setName("c2");
        ConversationEntity c3 = new ConversationEntity().setUsers(user1, user3).setName("c3");
        ConversationEntity c4 = new ConversationEntity().setUsers(user2, user3).setName("c4");
        conversationDao.persist(c1);
        conversationDao.persist(c2);
        conversationDao.persist(c3);
        conversationDao.persist(c4);

        //when
        Optional<ConversationEntity> conversation = conversationDao.getConversationWithUser(user1, user2);
        Optional<ConversationEntity> conversation2 = conversationDao.getConversationWithUser(user2, user3);

        //then
        assertThat(conversation.get().getName()).isEqualTo("c2");
        assertThat(conversation2.get().getName()).isEqualTo("c4");
    }

    @Test
    public void testConversationWithUserExists() throws Exception {
        //given
        UserEntity user1 = registerUser("user1", EMAIL, PASSWORD);
        UserEntity user2 = registerUser("user2", EMAIL, PASSWORD);

        //when
        Optional<ConversationEntity> conversation = conversationDao.getConversationWithUser(user1, user2);

        //then
        assertThat(conversation.isPresent()).isFalse();
    }

    @Test
    public void testGetMyGroups() throws Exception {
        //given
        UserEntity user1 = registerUser("user1", EMAIL, PASSWORD);
        UserEntity user2 = registerUser("user2", EMAIL, PASSWORD);
        UserEntity user3 = registerUser("user3", EMAIL, PASSWORD);

        ConversationEntity c1 = new ConversationEntity().setUsers(user1).setName("c1");
        ConversationEntity c2 = new ConversationEntity().setUsers(user1, user2).setName("c2").setGroup(true);
        ConversationEntity c3 = new ConversationEntity().setUsers(user1, user3).setName("c3").setGroup(true);
        ConversationEntity c4 = new ConversationEntity().setUsers(user2, user3).setName("c4").setGroup(true);
        conversationDao.persist(c1);
        conversationDao.persist(c2);
        conversationDao.persist(c3);
        conversationDao.persist(c4);

        //when
        List<ConversationEntity> conversations = conversationDao.getMyGroups(user1);

        //then
        assertThat(conversations).hasSize(2);
        assertThat(conversations).contains(c2);
        assertThat(conversations).contains(c3);
    }

    protected UserEntity registerUser(String name, String email, String password) {
        UserEntity userEntity = new UserEntity().setName(name).setEmail(email).setPassword(password);
        userService.register(userEntity);
        return userEntity;
    }
}