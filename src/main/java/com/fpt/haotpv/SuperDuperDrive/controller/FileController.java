package com.fpt.haotpv.SuperDuperDrive.controller;

import com.fpt.haotpv.SuperDuperDrive.entity.File;
import com.fpt.haotpv.SuperDuperDrive.entity.User;
import com.fpt.haotpv.SuperDuperDrive.service.FileService;
import com.fpt.haotpv.SuperDuperDrive.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping
public class FileController {

    private final FileService fileService;
    private final UserService userService;
    private final RedirectView redirectView = new RedirectView("result");
    private final int SUCCESS_RESULT = 1;
    private final int FAIL_RESULT = 2;

    private String errorMessage;
    private Integer userId;

    public FileController(FileService fileService,
                          UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("/uploadFile")
    public RedirectView uploadFile(@RequestParam MultipartFile fileUpload,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {

        if (fileUpload.getSize() == 0) {
            this.errorMessage = "Please select file to upload!!! ";
            redirectAttributes.addFlashAttribute("error", this.errorMessage);

            return this.redirectView;
        }

        this.getUserId(authentication);
        String fileName = fileUpload.getOriginalFilename();
        if (!this.fileService.isFileNameAvailable(fileName, this.userId)) {
            this.errorMessage = "The filename already exists";
            redirectAttributes.addFlashAttribute("error", this.errorMessage);

            return this.redirectView;
        }

        try {
            String contentType = fileUpload.getContentType();
            String fileSize = String.valueOf(fileUpload.getSize());
            byte[] fileData = fileUpload.getBytes();
            File file = new File(fileName, contentType, fileSize, this.userId, fileData);

            int row = this.fileService.uploadFile(file);
            redirectAttributes.addFlashAttribute("result", row > 0 ? SUCCESS_RESULT : FAIL_RESULT);
            redirectAttributes.addFlashAttribute("error", null);

            return this.redirectView;
        } catch (IOException ex) {
            this.errorMessage = ex.getMessage();
            redirectAttributes.addFlashAttribute("error", this.errorMessage);

            return this.redirectView;
        }
    }

    @GetMapping("/downloadFile")
    public Object downloadFile(@RequestParam Integer id,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes,
                               HttpServletResponse response) {

        this.getUserId(authentication);
        Optional<Integer> optionalId = Optional.ofNullable(id);
        if (optionalId.isEmpty()) {
            this.errorMessage = "BAD REQUEST! ID CANNOT NULL!";
            redirectAttributes.addFlashAttribute("error", this.errorMessage);

            return this.redirectView;
        }

        Optional<File> optionalFile = Optional.ofNullable(this.fileService.getFileById(optionalId.get()));
        if (optionalFile.isEmpty()) {
            this.errorMessage = "FILE NOT EXIST!";
            redirectAttributes.addFlashAttribute("error", this.errorMessage);

            return this.redirectView;
        }

        boolean isSameUser = userId.equals(optionalFile.get().getUserId());
        if (!isSameUser) {
            this.errorMessage = "CANNOT DOWNLOAD FILE OF ANOTHER USER!";
            redirectAttributes.addFlashAttribute("error", this.errorMessage);

            return this.redirectView;
        }

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = " + optionalFile.get().getFileName();
        response.setHeader(headerKey, headerValue);
        response.setContentType("application/octet-stream");
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(optionalFile.get().getFileData());
            outputStream.close();

            return null;
        } catch (IOException ex) {
            this.errorMessage = ex.getMessage();
            redirectAttributes.addFlashAttribute("error", this.errorMessage);

            return this.redirectView;
        }
    }

    @GetMapping("/deleteFile")
    public RedirectView deleteFile(@RequestParam Integer id,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {

        this.getUserId(authentication);
        Optional<Integer> optionalId = Optional.ofNullable(id);
        if (optionalId.isEmpty()) {
            this.errorMessage = "BAD REQUEST! ID CANNOT NULL!";
            redirectAttributes.addFlashAttribute("error", this.errorMessage);

            return this.redirectView;
        }

        Optional<File> optionalFile = Optional.ofNullable(this.fileService.getFileById(optionalId.get()));
        if (optionalFile.isEmpty()) {
            this.errorMessage = "FILE NOT EXIST!";
            redirectAttributes.addFlashAttribute("error", this.errorMessage);

            return this.redirectView;
        }

        boolean isSameUser = userId.equals(optionalFile.get().getUserId());
        if (!isSameUser) {
            this.errorMessage = "CANNOT DELETE FILE OF ANOTHER USER!";
            redirectAttributes.addFlashAttribute("error", this.errorMessage);

            return this.redirectView;
        }

        int row = this.fileService.deleteFile(optionalId.get());
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
