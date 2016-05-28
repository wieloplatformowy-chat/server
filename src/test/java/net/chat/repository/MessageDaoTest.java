package net.chat.repository;

import net.chat.config.authentication.TokenService;
import net.chat.entity.ConversationEntity;
import net.chat.entity.MessageEntity;
import net.chat.entity.UserConversationEntity;
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

import java.sql.Timestamp;
import java.util.LinkedList;
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
public class MessageDaoTest {
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "test@test.pl";
    private static final String CONVERSATION_NAME = "ConversationName";
    private static final String MESSAGE = "Message";

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

    @Autowired
    MessageDao messageDao;

    @Test
    public void testAddConversationsForUserLastPostSeenIsNull() throws Exception {
        //given
        UserEntity user1 = registerUser("user1", EMAIL, PASSWORD);

        ConversationEntity c1 = new ConversationEntity().setUsers(user1).setName("c1");
        conversationDao.persist(c1);
        createMessages(user1, c1, 5);

        //when
        List<Long> unreadMessages = messageDao.getConversationsWithUnreadMessagesId(user1);

        //then
        assertThat(unreadMessages).hasSize(1);
        assertThat(unreadMessages.get(0)).isEqualTo(c1.getId());
    }

    @Test
    public void testAddConversationsForUserLastPostSeenIsOld() throws Exception {
        //given
        UserEntity user1 = registerUser("user1", EMAIL, PASSWORD);

        ConversationEntity c1 = new ConversationEntity().setUsers(user1).setName("c1");
        conversationDao.persist(c1);
        List<MessageEntity> newMessages = createMessages(user1, c1, 5);

        setLastReadMessage(user1, c1, newMessages.get(2));

        //when
        List<Long> unreadMessages = messageDao.getConversationsWithUnreadMessagesId(user1);

        //then
        assertThat(unreadMessages).hasSize(1);
        assertThat(unreadMessages.get(0)).isEqualTo(c1.getId());
    }

    @Test
    public void testAddConversationsForUserAllRead() throws Exception {
        //given
        UserEntity user1 = registerUser("user1", EMAIL, PASSWORD);

        ConversationEntity c1 = new ConversationEntity().setUsers(user1).setName("c1");
        conversationDao.persist(c1);
        List<MessageEntity> newMessages = createMessages(user1, c1, 5);

        setLastReadMessage(user1, c1, newMessages.get(4));

        //when
        List<Long> unreadMessages = messageDao.getConversationsWithUnreadMessagesId(user1);

        //then
        assertThat(unreadMessages).isEmpty();
    }

    @Test
    public void testAddConversationsForUserNothingNew() throws Exception {
        //given
        UserEntity user1 = registerUser("user1", EMAIL, PASSWORD);

        ConversationEntity c1 = new ConversationEntity().setUsers(user1).setName("c1");
        conversationDao.persist(c1);

        //when
        List<Long> unreadMessages = messageDao.getConversationsWithUnreadMessagesId(user1);

        //then
        assertThat(unreadMessages).isEmpty();
    }

    @Test
    public void testAddConversationsForUserComplexCase() throws Exception {
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

        List<MessageEntity> newMessages1 = createMessages(user1, c1, 5);
        List<MessageEntity> newMessages2 = createMessages(user2, c2, 5);
        List<MessageEntity> newMessages3 = createMessages(user3, c3, 5);
        List<MessageEntity> newMessages4 = createMessages(user2, c4, 5);

        setLastReadMessage(user1, c1, newMessages1.get(2));
        setLastReadMessage(user1, c2, newMessages2.get(4));

        setLastReadMessage(user2, c2, newMessages2.get(1));
        setLastReadMessage(user3, c3, newMessages3.get(1));
        setLastReadMessage(user2, c4, newMessages4.get(2));

        //when
        List<Long> unreadMessages = messageDao.getConversationsWithUnreadMessagesId(user1);

        //then
        assertThat(unreadMessages).hasSize(2);
        assertThat(unreadMessages.get(0)).isEqualTo(c1.getId());
        assertThat(unreadMessages.get(1)).isEqualTo(c3.getId());
    }

    private void setLastReadMessage(UserEntity user, ConversationEntity conversation, MessageEntity lastSeenPost) {
        UserConversationEntity userConversation = userConversationDao.findByUserAndConversation(user, conversation);
        userConversation.setLastSeenPost(lastSeenPost);
        userConversationDao.merge(userConversation);
    }

    /**
     * Creates and persists Message entities with generate message = MESSAGE + i; i belong to [0, amount]
     *
     * @return All created entities
     */
    private List<MessageEntity> createMessages(UserEntity userEntity, ConversationEntity conversationEntity, int amount) {
        long data = 1464429746; //28.05.2016
        List<MessageEntity> entities = new LinkedList<>();
        for (int i = 1; i <= amount; i++) {
            MessageEntity messageEntity = new MessageEntity()
                    .setDate(new Timestamp(data + i * 10000))
                    .setUser(userEntity)
                    .setConversation(conversationEntity)
                    .setMessage(MESSAGE + i);
            messageDao.persist(messageEntity);
            entities.add(messageEntity);
        }
        return entities;
    }

    protected UserEntity registerUser(String name, String email, String password) {
        UserEntity userEntity = new UserEntity().setName(name).setEmail(email).setPassword(password);
        userService.register(userEntity);
        return userEntity;
    }
}