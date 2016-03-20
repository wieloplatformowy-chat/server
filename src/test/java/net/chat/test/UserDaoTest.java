package net.chat.test;

import net.chat.entity.User;
import net.chat.repository.UserDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:test-context.xml",
        "classpath:/META-INF/spring/applicationContext.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class UserDaoTest {
    @Autowired
    private UserDao userDao;

    @Test
    public void testFindById() {
        User user = userDao.findById(0l);

        Assert.assertEquals("John Smith", user.getName());
        Assert.assertEquals("john.smith@mailinator.com", user.getPassword());
    }

    @Test
    public void testFindByName() {
        User user = userDao.findByName("John Smith");

        Assert.assertEquals("John Smith", user.getName());
        Assert.assertEquals("john.smith@mailinator.com", user.getPassword());
    }

    @Test
    public void testRegister() {
        User user = new User();
        user.setName("Jane Doe");
        user.setPassword("jane.doe@mailinator.com");

        userDao.register(user);
        Long id = user.getId();
        Assert.assertNotNull(id);

        Assert.assertEquals(2, userDao.findAll().size());
        User newUser = userDao.findById(id);

        Assert.assertEquals("Jane Doe", newUser.getName());
        Assert.assertEquals("jane.doe@mailinator.com", newUser.getPassword());
    }

    @Test(expected = PersistenceException.class)
    public void testRegisterConstraintNameViolation() {
        User user = new User();
        user.setName("John Smith");
        user.setPassword("test");

        userDao.register(user);
    }

    @Test
    public void testFindAll() {
        User user = new User();
        user.setName("Jane Doe");
        user.setPassword("jane.doe@mailinator.com");
        userDao.register(user);

        List<User> users = userDao.findAll();
        Assert.assertEquals(2, users.size());
        User newUser = users.get(1);

        Assert.assertEquals("Jane Doe", newUser.getName());
        Assert.assertEquals("jane.doe@mailinator.com", newUser.getPassword());
    }
}
