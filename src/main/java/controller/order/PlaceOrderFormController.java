package controller.order;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.customer.CustomerController;
import controller.item.ItemController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import dto.*;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class PlaceOrderFormController implements Initializable {

    @FXML
    private JFXComboBox<String> cmbCustomerId;

    @FXML
    private JFXComboBox<String> cmbItemCode;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colItemCode;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblNetTotal;

    @FXML
    private Label lblTime;

    @FXML
    private TableView<CartTM> tblCart;

    @FXML
    private JFXTextField txtCity;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtOrderId;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private JFXTextField txtSalary;

    @FXML
    private JFXTextField txtStock;

    @FXML
    private JFXTextField txtUnitPrice;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observableValue, s, newValue) -> {
            loadCustomerData(newValue);
        });

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observableValue, s, newValue) -> {
            loadItemData(newValue);
        } );

        loadDateAndTime();
        loadCustomerIds();
        loadItemCodes();
    }

    ObservableList<CartTM> cart = FXCollections.observableArrayList();
    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        Double unitPrice= Double.valueOf(txtUnitPrice.getText());
        Integer qty = Integer.valueOf(txtQty.getText());
        Double total = unitPrice*qty;
        cart.add(
                new CartTM(
                        cmbItemCode.getValue(),
                        txtDescription.getText(),
                        qty,
                        unitPrice,
                        total
                )
        );
        tblCart.setItems(cart);
        calcNetTotal();
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {
        String orderId = txtOrderId.getText();
        String customerId = cmbCustomerId.getValue();
        //String orderData = lblDate.getText();

        LocalDate now = LocalDate.now();

        List<OrderDetail> orderDetails = new ArrayList<>();

        for (CartTM cartTM:cart){
            String itemCode= cartTM.getItemCode();
            Integer qty = cartTM.getQty();
            orderDetails.add(new OrderDetail(orderId,itemCode,qty,0.0));
        }


        try {
            if (new OrderController().placeOrder(new Order(orderId,now,customerId,orderDetails))){
                new Alert(Alert.AlertType.INFORMATION,"Order Placed ! ").show();
            }else {
                new Alert(Alert.AlertType.ERROR,"Order not Placed ! ").show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void loadDateAndTime(){
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));

        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime now = LocalTime.now();
            lblTime.setText(
                    now.getHour() + " : " + now.getMinute() + " : " + now.getSecond()
            );
        }),
                new KeyFrame(Duration.seconds(1))
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


    CustomerController customerController = new CustomerController();
    private void loadCustomerIds(){
        cmbCustomerId.setItems(customerController.getCustomerIds());
    }

    ItemController itemController=new ItemController();
    private void loadItemCodes(){
        cmbItemCode.setItems(itemController.getItemCodes());
    }
    private void loadCustomerData(String customerId){
        Customer customer = customerController.searchCustomer(customerId);
        txtName.setText(customer.getName());
        txtCity.setText(customer.getCity());
        txtSalary.setText(customer.getSalary().toString());
    }
    private void loadItemData(String itemCode){
        Item item = itemController.searchItem(itemCode);
        txtDescription.setText(item.getDescription());
        txtUnitPrice.setText(item.getUnitPrice().toString());
        txtStock.setText(item.getQOH().toString());
    }
    private void calcNetTotal(){
        Double netTotal=0.0;
        for (CartTM cartTM: cart) {
            netTotal+= cartTM.getTotal();
        }
        lblNetTotal.setText(netTotal.toString());
    }

}
