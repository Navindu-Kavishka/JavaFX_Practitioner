package service.custom.impl;

import dto.Item;
import entity.ItemEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.SuperDao;
import repository.custom.ItemDao;
import service.custom.ItemService;
import util.DaoType;

public class ItemServiceImpl implements ItemService {

    ItemDao dao = DaoFactory.getInstance().getDaoType(DaoType.ITEM);

    @Override
    public boolean addItem(Item item) {
        System.out.println("Item Service : "+item);
        return dao.save(new ModelMapper().map(item, ItemEntity.class));

    }

    @Override
    public boolean updateItem(Item item) {
        return dao.update(new ModelMapper().map(item,ItemEntity.class));
    }

    @Override
    public Item searchItem(String itemCode) {
        return new ModelMapper().map(dao.search(itemCode),Item.class);
    }

    @Override
    public boolean deleteItem(String itemCode) {
        return dao.delete(itemCode);
    }

    @Override
    public ObservableList<Item> getAllItems() {
        ObservableList<ItemEntity> itemEntityList = dao.findAll();
        ObservableList<Item> itemList = FXCollections.observableArrayList();
        itemEntityList.forEach(itemEntity -> {
            itemList.add(new ModelMapper().map(itemEntity,Item.class));
        });
        return itemList;
    }

    @Override
    public ObservableList<String> getItemCodes(){
        return dao.getIds();
    }
}
