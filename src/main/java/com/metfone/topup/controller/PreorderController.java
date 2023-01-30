package com.metfone.topup.controller;

import com.metfone.topup.model.MobileDeposit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Controller

public class PreorderController {
    @GetMapping("/preorder")
    public String preorder() {
        return "pre-order/index_pre-order";
    }
    @GetMapping("/test")
    public String test() {
        return "pre-order/test";
    }
    @GetMapping("/review-product")
    public String review() {
        return "pre-order/review-preorder";
    }
    @GetMapping("/confirmation")
    public String confrimation() {
        return "pre-order/comfirmation";
    }

}
