package com.metfone.topup.controller;

import com.metfone.topup.model.MobileDeposit;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PreorderController {
    @GetMapping("/preorder")
    public String createEmploye(Model model) {
        return "pre-order/index_pre-order";
    }
}
