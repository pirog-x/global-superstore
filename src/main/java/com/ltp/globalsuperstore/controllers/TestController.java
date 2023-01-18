package com.ltp.globalsuperstore.controllers;

import com.ltp.globalsuperstore.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/")
    public String getForm(Model model) {
        model.addAttribute("categories", Constants.CATEGORIES);
        return "form";
    }

    @GetMapping("/inventory")
    public String getInventory() {
        return "inventory";
    }

}
