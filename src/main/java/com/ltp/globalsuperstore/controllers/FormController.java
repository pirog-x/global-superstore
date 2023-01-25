package com.ltp.globalsuperstore.controllers;

import com.ltp.globalsuperstore.Constants;
import com.ltp.globalsuperstore.pojoClases.Item;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Controller
public class FormController {

    private final List<Item> items = new ArrayList<>();

    @GetMapping("/")
    public String getForm(Model model, @RequestParam(required = false) String id) {
        int index = getItemIndex(id);
        model.addAttribute("item", index == Constants.NOT_FOUND ? new Item() : items.get(index));
        model.addAttribute("items", items);
        return "form";
    }

    @GetMapping("/inventory")
    public String getInventory(Model model) {
        model.addAttribute("items", items);
        return "inventory";
    }

    @PostMapping("/submitItem")
    public String submitItemHandler(@NotNull @Valid Item item, BindingResult result,
                                    RedirectAttributes redirectAttributes) {
        if (item.getPrice() < item.getDiscount()) {
            result.rejectValue("price", "", "Price cannot be less than discount");
        }
        if (result.hasErrors()) return "form";

        int index = getItemIndex(item.getId());

        if (index == Constants.NOT_FOUND) {
            items.add(item);
            redirectAttributes.addFlashAttribute("status", Constants.SUCCESS_STATUS);
        } else {
            if (withinFiveDays(item.getDate(), items.get(index).getDate())) {
                redirectAttributes.addFlashAttribute("status", Constants.SUCCESS_STATUS);
                items.set(index, item);
            } else {
                redirectAttributes.addFlashAttribute("status", Constants.FAILED_STATUS);
            }
        }
        return "redirect:/inventory";
    }

    private Integer getItemIndex(String id) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(id)) return i;
        }
        return Constants.NOT_FOUND;
    }

    private boolean withinFiveDays(Date newDate, Date oldDate) {
        long diff = newDate.getTime() - oldDate.getTime();
        return TimeUnit.MILLISECONDS.toDays(diff) <= 5;
    }
}
