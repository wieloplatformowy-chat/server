package net.chat.config;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

@ApiIgnore
@Controller
public class DefaultController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String displaySortedMembers(Model model, HttpServletRequest request) {
        return "redirect:swagger-ui.html";
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String displaySortedMembers2(Model model, HttpServletRequest request) {
        return "test";
    }
}
