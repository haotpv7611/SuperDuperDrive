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
@RequestMapping("/note")
public class NoteController {

    private static final String RESULT_PAGE_URL = "result";

    private final NoteService noteService;
    private final UserService userService;

    private String errorMessage;
    private Integer userId;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping
    public RedirectView saveNote(Note note, Authentication authentication, RedirectAttributes redirectAttributes) {

        this.getUserId(authentication);
        Optional<Integer> optionalId = Optional.ofNullable(note.getNoteId());

        if (optionalId.isEmpty()) {
            note.setUserId(this.userId);
            int row = this.noteService.createNote(note);

            if (row >= 1) {
                redirectAttributes.addFlashAttribute("result", 1);
            } else {
                errorMessage = "CREATE FAIL!";
                redirectAttributes.addFlashAttribute("result", 2);
            }
        } else {
            Optional<Note> optionalNote = Optional.ofNullable(this.noteService.getNoteById(optionalId.get()));

            if (optionalNote.isPresent()) {
                boolean isSameUser = userId.equals(optionalNote.get().getUserId());

                if (isSameUser) {
                    int row = this.noteService.updateNote(note);

                    if (row >= 1) {
                        redirectAttributes.addFlashAttribute("result", 1);
                    } else {
                        errorMessage = "UPDATE FAIL!";
                        redirectAttributes.addFlashAttribute("result", 2);
                    }
                } else {
                    errorMessage = "CANNOT UPDATE NOTE OF ANOTHER USER!";
                    redirectAttributes.addFlashAttribute("error", errorMessage);
                }
            } else {
                errorMessage = "NOTE NOT EXIST!";
                redirectAttributes.addFlashAttribute("error", errorMessage);
            }
        }

//        redirectAttributes.addFlashAttribute("noteList", this.noteService.getAllNotesByUser(userId));

        return new RedirectView(RESULT_PAGE_URL);
    }

    @GetMapping("/deleteNote")
    public RedirectView deleteNote(Authentication authentication, @RequestParam Integer id, RedirectAttributes redirectAttributes) {
        this.getUserId(authentication);
        Optional<Integer> optionalId = Optional.ofNullable(id);

        if (optionalId.isPresent()) {
            Optional<Note> optionalNote = Optional.ofNullable(this.noteService.getNoteById(optionalId.get()));

            if (optionalNote.isPresent()) {
                boolean isSameUser = userId.equals(optionalNote.get().getUserId());

                if (isSameUser) {
                    int row = this.noteService.deleteNote(optionalId.get());

                    if (row >= 1) {
                        redirectAttributes.addFlashAttribute("result", 1);
                    } else {
                        errorMessage = "DELETE FAIL!";
                        redirectAttributes.addFlashAttribute("result", 2);
                    }
                } else {
                    errorMessage = "CANNOT DELETE NOTE OF ANOTHER USER!";
                    redirectAttributes.addFlashAttribute("error", errorMessage);
                }
            } else {
                errorMessage = "NOTE NOT EXIST!";
                redirectAttributes.addFlashAttribute("error", errorMessage);
            }
        } else {
            errorMessage = "BAD REQUEST! ID CANNOT NULL!";
            redirectAttributes.addFlashAttribute("error", errorMessage);
        }

//        redirectAttributes.addFlashAttribute("noteList", this.noteService.getAllNotesByUser(userId));

        return new RedirectView(RESULT_PAGE_URL);
    }

    private void getUserId(Authentication authentication) {

        String username = authentication.getName();
        Optional<User> optionalUser = Optional.ofNullable(this.userService.getUser(username));

        optionalUser.ifPresentOrElse(user -> userId = user.getId(), null);
    }
}
