package com.fpt.haotpv.SuperDuperDrive.controller;

import com.fpt.haotpv.SuperDuperDrive.entity.Note;
import com.fpt.haotpv.SuperDuperDrive.entity.User;
import com.fpt.haotpv.SuperDuperDrive.service.NoteService;
import com.fpt.haotpv.SuperDuperDrive.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@Controller
@RequestMapping
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;
    private final RedirectView redirectView = new RedirectView("result");
    private final int SUCCESS_RESULT = 1;
    private final int FAIL_RESULT = 2;

    private String errorMessage;
    private Integer userId;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("/note")
    public RedirectView saveNote(Note note,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {

        this.getUserId(authentication);
        int result = SUCCESS_RESULT;
        Optional<Integer> optionalId = Optional.ofNullable(note.getNoteId());
        if (optionalId.isEmpty()) {
            note.setUserId(this.userId);
            int row = this.noteService.createNote(note);
            if (row < 1) {
                result = FAIL_RESULT;
            }
        } else {
            Optional<Note> optionalNote = Optional.ofNullable(this.noteService.getNoteById(optionalId.get()));
            if (optionalNote.isEmpty()) {
                this.errorMessage = "NOTE NOT EXIST!";
                redirectAttributes.addFlashAttribute("error", this.errorMessage);

                return this.redirectView;
            }

            boolean isSameUser = this.userId.equals(optionalNote.get().getUserId());
            if (!isSameUser) {
                this.errorMessage = "CANNOT UPDATE NOTE OF ANOTHER USER!";
                redirectAttributes.addFlashAttribute("error", this.errorMessage);

                return this.redirectView;
            }

            int row = this.noteService.updateNote(note);
            if (row < 1) {
                result = FAIL_RESULT;
            }
        }

        redirectAttributes.addFlashAttribute("result", result);
        redirectAttributes.addFlashAttribute("error", null);

        return redirectView;
    }

    @GetMapping("/deleteNote")
    public RedirectView deleteNote(@RequestParam Integer id,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {

        this.getUserId(authentication);
        Optional<Integer> optionalId = Optional.ofNullable(id);
        if (optionalId.isEmpty()) {
            this.errorMessage = "BAD REQUEST! ID CANNOT NULL!";
            redirectAttributes.addFlashAttribute("error", this.errorMessage);

            return this.redirectView;
        }

        Optional<Note> optionalNote = Optional.ofNullable(this.noteService.getNoteById(optionalId.get()));
        if (optionalNote.isEmpty()) {
            this.errorMessage = "NOTE NOT EXIST!";
            redirectAttributes.addFlashAttribute("error", this.errorMessage);

            return this.redirectView;
        }

        boolean isSameUser = this.userId.equals(optionalNote.get().getUserId());
        if (!isSameUser) {
            this.errorMessage = "CANNOT DELETE NOTE OF ANOTHER USER!";
            redirectAttributes.addFlashAttribute("error", this.errorMessage);

            return this.redirectView;
        }

        int row = this.noteService.deleteNote(optionalId.get());
        redirectAttributes.addFlashAttribute("result", row > 0 ? SUCCESS_RESULT : FAIL_RESULT);
        redirectAttributes.addFlashAttribute("error", null);

        return redirectView;
    }

    private void getUserId(Authentication authentication) {

        String username = authentication.getName();
        Optional<User> optionalUser = Optional.ofNullable(this.userService.getUser(username));

        optionalUser.ifPresentOrElse(user -> this.userId = user.getId(), null);
    }
}
