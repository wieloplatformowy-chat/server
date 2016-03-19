package org.jboss.tools.example.springmvc.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import net.chat.repository.UserDao;

@Controller
@RequestMapping("/rest/users")
public class MemberRestController {
	@Autowired
	private UserDao userDao;

}
