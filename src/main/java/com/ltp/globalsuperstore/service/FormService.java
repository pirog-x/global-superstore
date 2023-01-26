package com.ltp.globalsuperstore.service;

import com.ltp.globalsuperstore.Constants;
import com.ltp.globalsuperstore.pojoClases.Item;
import com.ltp.globalsuperstore.repository.FormRepository;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FormService {
    private final FormRepository formRepository = new FormRepository();

    public Item getItemById(String id) {
        int index = getItemIndex(id);
        return index == Constants.NOT_FOUND ? new Item() : formRepository.getItem(index);
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

    public List<Item> getItems() {
        return formRepository.getItems();
    }

    public boolean isValidItemPrice(Item item) {
        if (item == null) return false;
        if (item.getPrice() == null) return false;

        return true;
    }

    public boolean isValidItemDiscount(Item item) {
        if (item == null) return false;
        if (item.getDiscount() == null) return false;

        return true;
    }

    public boolean priceIsBiggerThanDiscount(Item item) {
        if (item.getPrice() == null) return true;
        if (item.getPrice() < item.getDiscount()) return false;
        return true;
    }

    public String submitForm(Item item) {

        int index = getItemIndex(item.getId());

        if (index == Constants.NOT_FOUND) {
            addItem(item);
        } else {
            if (withinFiveDays(item.getDate(), formRepository.getItem(index).getDate())) {
                formRepository.setItem(index, item);
            } else {
                return Constants.FAILED_STATUS;
            }
        }
        return Constants.SUCCESS_STATUS;
    }

    public void addItem(Item item) {
        formRepository.addItem(item);
    }

    public boolean isValidItemDate(Item item) {
        if (item == null) return false;
        if (item.getDate() == null) return false;
        return true;
    }
}
