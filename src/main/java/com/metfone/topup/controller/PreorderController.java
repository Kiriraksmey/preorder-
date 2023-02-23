package com.metfone.topup.controller;

import com.metfone.topup.model.MobileDeposit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller

public class PreorderController {
    @GetMapping("/preorder")
    public String web() {
        return "pre-order/index_pre-order";
    }



}
