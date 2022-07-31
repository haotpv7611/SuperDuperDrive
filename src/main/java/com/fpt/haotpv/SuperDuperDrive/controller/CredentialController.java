package com.fpt.haotpv.SuperDuperDrive.controller;

import com.fpt.haotpv.SuperDuperDrive.entity.Credential;
import com.fpt.haotpv.SuperDuperDrive.entity.User;
import com.fpt.haotpv.SuperDuperDrive.service.CredentialService;
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
public class CredentialController {

    private static final String RESULT_PAGE_URL = "result";

    private final CredentialService credentialService;
    private final UserService userService;

    private String errorMessage;
    private Integer userId;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping("/credential")
    public RedirectView saveCredential(Credential credential, Authentication authentication, RedirectAttributes redirectAttributes) {

        this.getUserId(authentication);
        Optional<Integer> optionalId = Optional.ofNullable(credential.getCredentialId());

        if (optionalId.isEmpty()) {
            credential.setUserId(this.userId);
            int row = this.credentialService.createCredential(credential);

            if (row >= 1) {
                redirectAttributes.addFlashAttribute("result", 1);
            } else {
                errorMessage = "CREATE FAIL!";
                redirectAttributes.addFlashAttribute("result", 2);
            }
        } else {
            Optional<Credential> optionalCredential = Optional.ofNullable(this.credentialService.getCredentialById(optionalId.get()));

            if (optionalCredential.isPresent()) {
                boolean isSameUser = userId.equals(optionalCredential.get().getUserId());

                if (isSameUser) {
                    credential.setKey(optionalCredential.get().getKey());
                    int row = this.credentialService.updateCredential(credential);

                    if (row >= 1) {
                        redirectAttributes.addFlashAttribute("result", 1);
                    } else {
                        errorMessage = "UPDATE FAIL!";
                        redirectAttributes.addFlashAttribute("result", 2);
                    }
                } else {
                    errorMessage = "CANNOT UPDATE CREDENTIAL OF ANOTHER USER!";
                    redirectAttributes.addFlashAttribute("error", errorMessage);
                }
            } else {
                errorMessage = "CREDENTIAL NOT EXIST!";
                redirectAttributes.addFlashAttribute("error", errorMessage);
            }
        }

        return new RedirectView(RESULT_PAGE_URL);
    }

    @GetMapping("/deleteCredential")
    public RedirectView deleteNote(Authentication authentication, @RequestParam Integer id, RedirectAttributes redirectAttributes) {
        this.getUserId(authentication);
        Optional<Integer> optionalId = Optional.ofNullable(id);

        if (optionalId.isPresent()) {
            Optional<Credential> optionalNote = Optional.ofNullable(this.credentialService.getCredentialById(optionalId.get()));

            if (optionalNote.isPresent()) {
                boolean isSameUser = userId.equals(optionalNote.get().getUserId());

                if (isSameUser) {
                    int row = this.credentialService.deleteCredential(optionalId.get());

                    if (row >= 1) {
                        redirectAttributes.addFlashAttribute("result", 1);
                    } else {
                        errorMessage = "DELETE FAIL!";
                        redirectAttributes.addFlashAttribute("result", 2);
                    }
                } else {
                    errorMessage = "CANNOT DELETE CREDENTIAL OF ANOTHER USER!";
                    redirectAttributes.addFlashAttribute("error", errorMessage);
                }
            } else {
                errorMessage = "CREDENTIAL NOT EXIST!";
                redirectAttributes.addFlashAttribute("error", errorMessage);
            }
        } else {
            errorMessage = "BAD REQUEST! ID CANNOT NULL!";
            redirectAttributes.addFlashAttribute("error", errorMessage);
        }

        return new RedirectView(RESULT_PAGE_URL);
    }

    private void getUserId(Authentication authentication) {

        String username = authentication.getName();
        Optional<User> optionalUser = Optional.ofNullable(this.userService.getUser(username));

        optionalUser.ifPresentOrElse(user -> userId = user.getId(), null);
    }
}
