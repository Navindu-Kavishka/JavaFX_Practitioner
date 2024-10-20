package repository.custom.impl;

import dto.Item;
import entity.ItemEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.modelmapper.ModelMapper;
import repository.custom.ItemDao;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemDaoImpl implements ItemDao {
    @Override
    public boolean save(ItemEntity item) {
        System.out.println("Item Repository : "+item);
        String SQL = "INSERT INTO item VALUES (?,?,?,?,?)";
        try {
            return CrudUtil.execute(
                    SQL,
                    item.getCode(),
                    item.getDescription(),
                    item.getPackSize(),
                    item.getUnitPrice(),
                    item.getQOH()
            );
        } catch (SQLException e) {
            System.out.println("SQL Error : "+e);
        }
        return false;
    }

    @Override
    public boolean update(ItemEntity item) {
        String SQL= "UPDATE item SET Description=?,PackSize=?,UnitPrice=?,QtyOnHand=? WHERE ItemCode=?";
        try {
            return CrudUtil.execute(
                    SQL,
                    item.getDescription(),
                    item.getPackSize(),
                    item.getUnitPrice(),
                    item.getQOH(),
                    item.getCode()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(String itemCode) {
        String SQL = "DELETE FROM item WHERE ItemCode=?";
        try {
            return CrudUtil.execute(SQL,itemCode);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObservableList<ItemEntity> findAll() {
        String SQL="SELECT * FROM item";
        ObservableList<Item> itemObservableList = FXCollections.observableArrayList();
        try {
            ResultSet rset = CrudUtil.execute(SQL);

            while (rset.next()){
                itemObservableList.add(new Item(
                        rset.getString(1),
                        rset.getString(2),
                        rset.getString(3),
                        rset.getDouble(4),
                        rset.getInt(5)
                ));
            }
            ObservableList<ItemEntity> itemEntityList = FXCollections.observableArrayList();
            itemObservableList.forEach(item -> {
                itemEntityList.add(new ModelMapper().map(item,ItemEntity.class));
            });
            return itemEntityList;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Item search(String itemCode) {
        try {
            String SQL="SELECT * FROM item WHERE ItemCode=?";
            ResultSet rset = CrudUtil.execute(SQL,itemCode);
            rset.next();
            return new Item(
                    rset.getString(1),
                    rset.getString(2),
                    rset.getString(3),
                    rset.getDouble(4),
                    rset.getInt(5)
            );

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObservableList<String> getIds() {
        ObservableList<ItemEntity> allItems = findAll();
        ObservableList<String> itemCodeList = FXCollections.observableArrayList();

        allItems.forEach(item -> {
            itemCodeList.add(item.getCode());
        });
        return itemCodeList;
    }
}
