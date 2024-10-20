package controller.item;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import dto.Item;
import dto.OrderDetail;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ItemController implements ItemService {


    public boolean updateStock(List<OrderDetail> orderDetails) throws SQLException {
        for (OrderDetail orderDetail : orderDetails){
            if (!updateStock(orderDetail)) {
                return false;
            }
        }
        return true;
    }

    public boolean updateStock(OrderDetail orderDetail) throws SQLException {
        return CrudUtil.execute("UPDATE item set QtyOnHand=QtyOnHand-? WHERE ItemCode=?",
                orderDetail.getQty(),
                orderDetail.getItemCode()
        );
    }
}
