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

    @RequestMapping(value = "/www", method = RequestMethod.GET)
    public String displayWWW(Model model, HttpServletRequest request) {
        return "index";
    }

    @RequestMapping(value = "/www/login", method = RequestMethod.GET)
    public String displayLogin(Model model, HttpServletRequest request) {
        return "login";
    }

    @RequestMapping(value = "/www/login", method = RequestMethod.POST)
    public String displayLoginPost(Model model, HttpServletRequest request) {
        return "redirect:/www";
    }

    @RequestMapping(value = "/www/register", method = RequestMethod.GET)
    public String displayRegister(Model model, HttpServletRequest request) {
        return "register";
    }

    @RequestMapping(value = "/www/remindPassword", method = RequestMethod.GET)
    public String displayRemindPassword(Model model, HttpServletRequest request) {
        return "remindPassword";
    }

    @RequestMapping(value = "/www/remindPassword", method = RequestMethod.POST)
    public String displayRemindPasswordPost(Model model, HttpServletRequest request) {
        return "redirect:/www";
    }

    @RequestMapping(value = "/www/register", method = RequestMethod.POST)
    public String displayRegisterPost(Model model, HttpServletRequest request) {
        return "redirect:/www/login";
    }
}
