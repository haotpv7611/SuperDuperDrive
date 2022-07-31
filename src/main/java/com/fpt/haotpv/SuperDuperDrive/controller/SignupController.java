package com.fpt.haotpv.SuperDuperDrive.controller;

import com.fpt.haotpv.SuperDuperDrive.entity.User;
import com.fpt.haotpv.SuperDuperDrive.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.PostConstruct;

@Controller
@RequestMapping("/signup")
public class SignupController {

    private final String SIGNUP_URL = "signup";

    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String signupView() {

        return SIGNUP_URL;
    }

    @PostMapping
    public RedirectView signupUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {

        final String DUPLICATE_USERNAME = "The username already exists.";
        final String CREATE_FAILED = "There was an error signing you up. Please try again.";

        String signupError = null;
        String url = SIGNUP_URL;

        //check duplicate username
        if (userService.isUsernameAvailable(user.getUsername())) {
            Integer row = userService.createUser(user);

            if (row < 0) {
                signupError = "There was an error signing you up. Please try again.";
            }
        } else {
            signupError = "The username already exists.";
        }

        if (signupError == null) {
            redirectAttributes.addFlashAttribute("signupSuccess", true);
            url = "login";
        } else {
            redirectAttributes.addFlashAttribute("signupError", signupError);
        }

        return new RedirectView(url);
    }

    @PostConstruct
    private void initUser() {

        final String temp = "123";
        System.out.println("Init user123");
        User user = new User();
        user.setFirstName(temp);
        user.setLastName(temp);
        user.setUsername(temp);
        user.setPassword(temp);
        userService.createUser(user);
        System.out.println(user);
    }
}
