package com.fpt.haotpv.SuperDuperDrive.controller;

import com.fpt.haotpv.SuperDuperDrive.entity.User;
import com.fpt.haotpv.SuperDuperDrive.service.NoteService;
import com.fpt.haotpv.SuperDuperDrive.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final NoteService noteService;
    private final UserService userService;

    private Integer userId;

    public HomeController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping
    public String homeView(Authentication authentication, Model model) {

        this.getUserId(authentication);
        model.addAttribute("noteList", this.noteService.getAllNotesByUser(userId));

        return "home";
    }

    private void getUserId(Authentication authentication) {

        String username = authentication.getName();
        Optional<User> optionalUser = Optional.ofNullable(this.userService.getUser(username));

        optionalUser.ifPresentOrElse(user -> userId = user.getId(), null);
    }
}
