package com.fpt.haotpv.SuperDuperDrive.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(value = HttpStatus.PAYLOAD_TOO_LARGE)
    public ModelAndView handleSizeLimitExceed() {

        ModelAndView mav = new ModelAndView();
        mav.addObject("error", "Cannot upload file exceeds 1 MB");
        mav.setViewName("result");

        return mav;
    }
}
