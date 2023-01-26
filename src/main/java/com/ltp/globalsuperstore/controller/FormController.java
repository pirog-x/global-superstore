package com.ltp.globalsuperstore.controller;

import com.ltp.globalsuperstore.pojoClases.Item;
import com.ltp.globalsuperstore.service.FormService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
public class FormController {

    FormService formService = new FormService();

    @GetMapping("/")
    public String getForm(Model model, @RequestParam(required = false) String id) {
        model.addAttribute("item", formService.getItemById(id));
        return "form";
    }

    @GetMapping("/inventory")
    public String getInventory(Model model) {
        model.addAttribute("items", formService.getItems());
        return "inventory";
    }

    @PostMapping("/submitItem")
    public String submitItemHandler(@Valid Item item, BindingResult result,
                                    RedirectAttributes redirectAttributes) {
        if (!formService.isValidItemPrice(item))
            result.rejectValue("price", "", "Field cannot be empty");
        if (!formService.isValidItemDiscount(item))
            result.rejectValue("discount", "", "Field cannot be empty");
        if (!formService.priceIsBiggerThanDiscount(item))
            result.rejectValue("price", "", "Price cannot be less than discount");
        if (!formService.isValidItemDate(item))
            result.rejectValue("date", "", "Field cannot be empty");

        if (result.hasErrors()) return "form";

        redirectAttributes.addFlashAttribute("status", formService.submitForm(item));

        return "redirect:/inventory";
    }
}
