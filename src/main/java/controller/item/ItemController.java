package controller.item;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
}
