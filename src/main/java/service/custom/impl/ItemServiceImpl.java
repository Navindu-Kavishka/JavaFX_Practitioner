package service.custom.impl;

import dto.Item;
import javafx.collections.ObservableList;
import service.custom.ItemService;

public class ItemServiceImpl implements ItemService {
    @Override
    public boolean addItem(Item item) {
        return false;
    }

    @Override
    public boolean updateItem(Item item) {
        return false;
    }

    @Override
    public Item searchItem(String itemCode) {
        return null;
    }

    @Override
    public boolean deleteItem(String itemCode) {
        return false;
    }

    @Override
    public ObservableList<Item> getAllItems() {
        return null;
    }
}
