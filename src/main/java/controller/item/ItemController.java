package controller.item;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Item;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemController implements ItemService {

    @Override
    public boolean addItem(Item item) {
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateItem(Item item) {
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
    public Item searchItem(String itemCode) {
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
    public boolean deleteItem(String itemCode) {
        String SQL = "DELETE FROM item WHERE ItemCode=?";
        try {
            return CrudUtil.execute(SQL,itemCode);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObservableList<Item> getAllItems() {
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
            return itemObservableList;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ObservableList<String> getItemCodes(){
        ObservableList<Item> allItems = getAllItems();
        ObservableList<String> itemCodeList = FXCollections.observableArrayList();

        allItems.forEach(item -> {
            itemCodeList.add(item.getCode());
        });
        return itemCodeList;
    }
}
