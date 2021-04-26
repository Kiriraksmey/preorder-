package com.metfone.topup.exception;

import com.metfone.topup.model.CheckIsdnResponse;
import com.metfone.topup.model.MobileDeposit;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.ui.ModelMap;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ModelAndView handleError405(HttpServletRequest request, Exception e) {
        ModelMap model = new ModelMap();
        model.addAttribute("mobileDeposit", new MobileDeposit());
        return new ModelAndView("redirect:/", model);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ModelAndView handleError400(Exception e, HttpServletRequest request) {
        ModelMap model = new ModelMap();
        model.addAttribute("mobileDeposit", new MobileDeposit());
        return new ModelAndView("redirect:/", model);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ModelAndView handleError500(Exception e, HttpServletRequest request) {
        ModelMap model = new ModelMap();
        model.addAttribute("mobileDeposit", new MobileDeposit());
        return new ModelAndView("redirect:/", model);
    }
}