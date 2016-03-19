package net.chat.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.chat.entity.User;
import net.chat.repository.UserDao;

@Controller
@RequestMapping("/rest/users")
public class UserRestController {
	@Autowired
	private UserDao userDao;

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<User> listAllMembers() {
		return userDao.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody User findMemberById(@PathVariable("id") Long id) {
		return userDao.findById(id);
	}
}
