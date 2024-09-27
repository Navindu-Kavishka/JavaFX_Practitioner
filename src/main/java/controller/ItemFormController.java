package controller;

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

            try {
                PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO item VALUES (?,?,?,?,?)");
                stm.setObject(1,item.getCode());
                stm.setObject(2,item.getDescription());
                stm.setObject(3,item.getPackSize());
                stm.setObject(4,item.getUnitPrice());
                stm.setObject(5,item.getQOH());
                if (stm.executeUpdate()>0){
                    new Alert(Alert.AlertType.INFORMATION,"Item Added :)").show();
                    loadTable();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.INFORMATION,"Item not Added :(").show();
                System.out.println(e.getMessage());
            }
        }catch (Throwable ex){
            new Alert(Alert.AlertType.INFORMATION,"Item not Added :(").show();
            System.out.println(ex.getMessage());
        }

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        try {
            int res = DBConnection.getInstance().getConnection().createStatement().executeUpdate("DELETE FROM item WHERE ItemCode='" + txtItemCode.getText() + "'");

            if (res>0){
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
            stm = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM item WHERE ItemCode=?");
            stm.setObject(1,txtItemCode.getText());
            ResultSet rset = stm.executeQuery();
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
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement("UPDATE item SET Description=?,PackSize=?,UnitPrice=?,QtyOnHand=? WHERE ItemCode=?");
            stm.setObject(1,item.getDescription());
            stm.setObject(2,item.getPackSize());
            stm.setObject(3,item.getUnitPrice());
            stm.setObject(4,item.getQOH());
            stm.setObject(5,item.getCode());

            int i = stm.executeUpdate();
            if (i>0){
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
        loadTable();

        tblItems.getSelectionModel().selectedItemProperty().addListener((observableValue, item, newValue) -> {
            if (newValue!=null){
                setValueToText(newValue);
            }
        });
    }

    private void loadTable() {
        ObservableList<Item> itemObserverList = FXCollections.observableArrayList();

        try {
            ResultSet rset = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * FROM item");

            colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
            colDsc.setCellValueFactory(new PropertyValueFactory<>("description"));
            colPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
            colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
            colQOH.setCellValueFactory(new PropertyValueFactory<>("QOH"));

            while (rset.next()){
                Item item = new Item(
                        rset.getString("ItemCode"),
                        rset.getString("Description"),
                        rset.getString("PackSize"),
                        rset.getDouble("UnitPrice"),
                        rset.getInt("QtyOnHand")
                );
                itemObserverList.add(item);
            }
            tblItems.setItems(itemObserverList);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }
}
