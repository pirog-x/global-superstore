package com.ltp.globalsuperstore.controllers;

import com.ltp.globalsuperstore.Constants;
import com.ltp.globalsuperstore.pojoClases.Item;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
public class TestController {

    private final List<Item> items = new ArrayList<>();

    @GetMapping("/")
    public String getForm(Model model) {
        model.addAttribute("categories", Constants.CATEGORIES);
        model.addAttribute("item", new Item());
        model.addAttribute("items", items);
        return "form";
    }

    @GetMapping("/inventory")
    public String getInventory(Model model) {
        model.addAttribute("items", items);
        return "inventory";
    }

    @PostMapping("/submitItem")
    public String submitItemHandler(Item item) {
        items.add(item);
        return "redirect:/inventory";
    }
}
