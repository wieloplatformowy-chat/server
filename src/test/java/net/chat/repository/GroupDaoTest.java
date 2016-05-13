package net.chat.rest;

import net.chat.config.authentication.TokenService;
import net.chat.entity.GroupEntity;
import net.chat.entity.UserEntity;
import net.chat.repository.FriendDao;
import net.chat.repository.GroupDao;
import net.chat.repository.UserDao;
import net.chat.repository.UserGroupDao;
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
public class GroupDaoTest {
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
    GroupDao groupDao;

    @Autowired
    UserGroupDao userGroupDao;

    @Test
    public void testAddConversationsForUser() throws Exception {
        //given
        UserEntity newUser = registerUser(USERNAME, EMAIL, PASSWORD);
        GroupEntity conversation1 = new GroupEntity().setName("name");
        GroupEntity conversation2 = new GroupEntity().setName("name2");
        newUser.setGroups(conversation1, conversation2);
        userDao.merge(newUser);

        //then
        UserEntity loaded = userDao.findById(newUser.getId());
        assertThat(loaded.getGroups().get(0).getName()).isEqualTo("name");
        assertThat(loaded.getGroups().get(1).getName()).isEqualTo("name2");
        assertThat(userGroupDao.findAll()).hasSize(2);
    }

    @Test
    public void testAddUsersToConversation() throws Exception {
        //given
        UserEntity user1 = registerUser("user1", EMAIL, PASSWORD);
        UserEntity user2 = registerUser("user2", EMAIL, PASSWORD);

        //when
        GroupEntity group = new GroupEntity().setName(CONVERSATION_NAME);
        group.setUsers(user1, user2);
        groupDao.persist(group);

        //then
        GroupEntity loaded = groupDao.findById(group.getId());
        assertThat(loaded.getUsers().get(0).getName()).isEqualTo("user1");
        assertThat(loaded.getUsers().get(1).getName()).isEqualTo("user2");
        assertThat(userGroupDao.findAll()).hasSize(2);
    }

    protected UserEntity registerUser(String name, String email, String password) {
        UserEntity userEntity = new UserEntity().setName(name).setEmail(email).setPassword(password);
        userService.register(userEntity);
        return userEntity;
    }
}