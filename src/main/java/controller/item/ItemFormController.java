package controller.item;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Item;
import util.CrudUtil;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ItemFormController implements Initializable {

    @FXML
    private TableColumn<?, ?> colCode;

    @FXML
    private TableColumn<?, ?> colDsc;

    @FXML
    private TableColumn<?, ?> colPackSize;

    @FXML
    private TableColumn<?, ?> colQOH;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private TableView<Item> tblItems;

    @FXML
    private JFXTextField txtItemCode;

    @FXML
    private JFXTextArea txtDescription;

    @FXML
    private JFXTextField txtPackSze;

    @FXML
    private JFXTextField txtQtyOnHand;

    @FXML
    private JFXTextField txtUnitPrice;

    ItemService itemController = new ItemController();

    @FXML
    void btnAddOnAction(ActionEvent event) {

        try {
            Item item= new Item(
                    txtItemCode.getText(),
                    txtDescription.getText(),
                    txtPackSze.getText(),
                    Double.parseDouble(txtUnitPrice.getText()),
                    Integer.parseInt(txtQtyOnHand.getText())
            );


            boolean isAdded =itemController.addItem(item);
            if (isAdded){
                new Alert(Alert.AlertType.INFORMATION,"Item Added :)").show();
                loadTable();
            }
        }catch (Throwable ex){
            new Alert(Alert.AlertType.INFORMATION,"Item not Added :(").show();
            System.out.println(ex.getMessage());
        }

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        try {
            String SQL = "DELETE FROM item WHERE ItemCode=?";
            boolean isDeleted = CrudUtil.execute(SQL,txtItemCode.getText());
            if (isDeleted){
                new Alert(Alert.AlertType.INFORMATION,""+txtItemCode.getText()+": Item Deleted !!").show();
                loadTable();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.INFORMATION,""+txtItemCode.getText()+": Item not Deleted !!").show();
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {

    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {

        PreparedStatement stm = null;
        try {
            String SQL="SELECT * FROM item WHERE ItemCode=?";
            ResultSet rset = CrudUtil.execute(SQL,txtItemCode.getText());
            rset.next();
            setValueToText(new Item(
                    rset.getString(1),
                    rset.getString(2),
                    rset.getString(3),
                    rset.getDouble(4),
                    rset.getInt(5)
            ));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void setValueToText(Item newValue) {
        txtItemCode.setText(newValue.getCode());
        txtDescription.setText(newValue.getDescription());
        txtPackSze.setText(newValue.getPackSize());
        txtUnitPrice.setText(newValue.getUnitPrice().toString());
        txtQtyOnHand.setText(newValue.getQOH().toString());
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

        Item item= new Item(
                txtItemCode.getText(),
                txtDescription.getText(),
                txtPackSze.getText(),
                Double.parseDouble(txtUnitPrice.getText()),
                Integer.parseInt(txtQtyOnHand.getText())
        );

        try {
            String SQL= "UPDATE item SET Description=?,PackSize=?,UnitPrice=?,QtyOnHand=? WHERE ItemCode=?";

            boolean isUpdated = CrudUtil.execute(
                    SQL,
                    item.getDescription(),
                    item.getPackSize(),
                    item.getUnitPrice(),
                    item.getQOH(),
                    item.getCode()
                    );
            if (isUpdated){
                new Alert(Alert.AlertType.INFORMATION,"Item Updated!").show();
                loadTable();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Item Not Updated!").show();
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDsc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQOH.setCellValueFactory(new PropertyValueFactory<>("QOH"));

        loadTable();

        tblItems.getSelectionModel().selectedItemProperty().addListener((observableValue, item, newValue) -> {
            if (newValue!=null){
                setValueToText(newValue);
            }
        });
    }

    private void loadTable() {
        ObservableList<Item> itemObserverList = itemController.getAllItems();

        tblItems.setItems(itemObserverList);


    }
}
