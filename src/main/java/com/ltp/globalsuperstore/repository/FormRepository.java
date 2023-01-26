package com.ltp.globalsuperstore.repository;

import com.ltp.globalsuperstore.pojoClases.Item;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class FormRepository {
    private final List<Item> items = new ArrayList<>();

    // CRUD - create - read - update - delete
    public void addItem(@NotNull Item item) {
        items.add(item);
    }

    public Item getItem(int index) {
        if (!isCorrectIndex(index)) throw new IllegalArgumentException("invalid index");
        return items.get(index);
    }

    public void setItem(int index, @NotNull Item item) {
        if (!isCorrectIndex(index)) throw new IllegalArgumentException("invalid index");
        items.set(index, item);
    }

    public void deleteItem(int index) {
        items.remove(index);
    }

    public List<Item> getItems() {
        return items;
    }

    private boolean isCorrectIndex(int index) {
        return !(index < 0 || index >= items.size());
    }
}
