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

    private final CredentialService credentialService;
    private final UserService userService;
    private final RedirectView redirectView = new RedirectView("result");
    private final int SUCCESS_RESULT = 1;
    private final int FAIL_RESULT = 2;

    private String errorMessage;
    private Integer userId;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping("/credential")
    public RedirectView saveCredential(Credential credential,
                                       Authentication authentication,
                                       RedirectAttributes redirectAttributes) {

        this.getUserId(authentication);
        int result = SUCCESS_RESULT;
        Optional<Integer> optionalId = Optional.ofNullable(credential.getCredentialId());
        if (optionalId.isEmpty()) {
            credential.setUserId(this.userId);
            int row = this.credentialService.createCredential(credential);
            if (row < 1) {
                result = FAIL_RESULT;
            }
        } else {
            Optional<Credential> optionalCredential = Optional.ofNullable(this.credentialService.getCredentialById(optionalId.get()));
            if (optionalCredential.isEmpty()) {
                this.errorMessage = "CREDENTIAL NOT EXIST!";
                redirectAttributes.addFlashAttribute("error", this.errorMessage);

                return this.redirectView;
            }
            boolean isSameUser = this.userId.equals(optionalCredential.get().getUserId());
            if (!isSameUser) {
                this.errorMessage = "CANNOT UPDATE CREDENTIAL OF ANOTHER USER!";
                redirectAttributes.addFlashAttribute("error", this.errorMessage);

                return this.redirectView;
            }

            credential.setKey(optionalCredential.get().getKey());
            int row = this.credentialService.updateCredential(credential);
            if (row < 1) {
                result = FAIL_RESULT;
            }
        }

        redirectAttributes.addFlashAttribute("result", result);
        redirectAttributes.addFlashAttribute("error", null);

        return this.redirectView;
    }

    @GetMapping("/deleteCredential")
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

        Optional<Credential> optionalCredential = Optional.ofNullable(this.credentialService.getCredentialById(optionalId.get()));
        if (optionalCredential.isEmpty()) {
            this.errorMessage = "CREDENTIAL NOT EXIST!";
            redirectAttributes.addFlashAttribute("error", this.errorMessage);

            return this.redirectView;
        }

        boolean isSameUser = this.userId.equals(optionalCredential.get().getUserId());
        if (!isSameUser) {
            this.errorMessage = "CANNOT DELETE CREDENTIAL OF ANOTHER USER!";
            redirectAttributes.addFlashAttribute("error", this.errorMessage);

            return this.redirectView;
        }

        int row = this.credentialService.deleteCredential(optionalId.get());
        redirectAttributes.addFlashAttribute("result", row > 0 ? SUCCESS_RESULT : FAIL_RESULT);
        redirectAttributes.addFlashAttribute("error", null);

        return this.redirectView;
    }

    private void getUserId(Authentication authentication) {

        String username = authentication.getName();
        Optional<User> optionalUser = Optional.ofNullable(this.userService.getUser(username));

        optionalUser.ifPresentOrElse(user -> this.userId = user.getId(), null);
    }
}
