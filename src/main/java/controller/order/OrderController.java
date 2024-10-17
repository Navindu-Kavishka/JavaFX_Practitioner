package controller.order;

import controller.item.ItemController;
import db.DBConnection;
import model.Order;
import model.OrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderController {

    public Boolean placeOrder(Order order) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement psTm = connection.prepareStatement("INSERT INTO orders VALUE(?,?,?)");
            psTm.setObject(1,order.getOrderId());
            psTm.setObject(2,order.getOrderDate());
            psTm.setObject(3,order.getCustomerId());
            boolean isOrderAdded = psTm.executeUpdate() > 0;

            if (isOrderAdded){
                boolean isOrderDetailAdded = OrderDetailController.addOrderDetail(order.getOrderDetails());

                if (isOrderDetailAdded){
                    boolean isUpdateStock = new ItemController().updateStock(order.getOrderDetails());

                    if (isUpdateStock){
                        connection.commit();
                        return true;
                    }
                }
            }
            return true;
        }finally {
            connection.setAutoCommit(true);
        }
    }
}
