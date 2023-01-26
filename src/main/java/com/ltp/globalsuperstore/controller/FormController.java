package com.ltp.globalsuperstore.controller;

import com.ltp.globalsuperstore.Constants;
import com.ltp.globalsuperstore.pojoClases.Item;
import com.ltp.globalsuperstore.repository.FormRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@Controller
public class FormController {

    private final FormRepository formRepository = new FormRepository();

    @GetMapping("/")
    public String getForm(Model model, @RequestParam(required = false) String id) {
        int index = getItemIndex(id);
        model.addAttribute("item", index == Constants.NOT_FOUND ? new Item() : formRepository.getItem(index));
        model.addAttribute("items", formRepository.getItems());
        return "form";
    }

    @GetMapping("/inventory")
    public String getInventory(Model model) {
        model.addAttribute("items", formRepository.getItems());
        return "inventory";
    }

    @PostMapping("/submitItem")
    public String submitItemHandler(@Valid Item item, BindingResult result,
                                    RedirectAttributes redirectAttributes) {
        if (item.getPrice() == null || item.getDiscount() == null) {
            if (item.getPrice() == null) {
                result.rejectValue("price", "", "Field cannot be empty");
            }
            if (item.getDiscount() == null) {
                result.rejectValue("discount", "", "Field cannot be empty");
            }
            return "form";
        }
        if (item.getPrice() < item.getDiscount()) {
            result.rejectValue("price", "", "Price cannot be less than discount");
        }
        if (result.hasErrors()) return "form";

        int index = getItemIndex(item.getId());

        if (index == Constants.NOT_FOUND) {
            formRepository.addItem(item);
            redirectAttributes.addFlashAttribute("status", Constants.SUCCESS_STATUS);
        } else {
            if (withinFiveDays(item.getDate(), formRepository.getItem(index).getDate())) {
                redirectAttributes.addFlashAttribute("status", Constants.SUCCESS_STATUS);
                formRepository.setItem(index, item);
            } else {
                redirectAttributes.addFlashAttribute("status", Constants.FAILED_STATUS);
            }
        }
        return "redirect:/inventory";
    }

    private Integer getItemIndex(String id) {
        for (int i = 0; i < formRepository.getItems().size(); i++) {
            if (formRepository.getItem(i).getId().equals(id)) return i;
        }
        return Constants.NOT_FOUND;
    }

    private boolean withinFiveDays(Date newDate, Date oldDate) {
        long diff = newDate.getTime() - oldDate.getTime();
        return TimeUnit.MILLISECONDS.toDays(diff) <= 5;
    }
}
