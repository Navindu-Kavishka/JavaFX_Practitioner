package controller.item;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import dto.Item;
import service.ServiceFactory;
import service.SuperService;
import util.ServiceType;

import java.io.IOException;
import java.net.URL;
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
            Item item= new Item(
                    txtItemCode.getText(),
                    txtDescription.getText(),
                    txtPackSze.getText(),
                    Double.parseDouble(txtUnitPrice.getText()),
                    Integer.parseInt(txtQtyOnHand.getText())
            );

        service.custom.ItemService service = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);

        if (service.addItem(item)){
            new Alert(Alert.AlertType.INFORMATION,"Item Added :)").show();
                loadTable();
        }else {
            new Alert(Alert.AlertType.INFORMATION, "Item not Added :(").show();
        }

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (itemController.deleteItem(txtItemCode.getText())){
            new Alert(Alert.AlertType.INFORMATION,""+txtItemCode.getText()+": Item Deleted !!").show();
            loadTable();
        }else {
            new Alert(Alert.AlertType.INFORMATION,""+txtItemCode.getText()+": Item not Deleted !!").show();
        }
    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {

    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        setValueToText(itemController.searchItem(txtItemCode.getText()));
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

            if (itemController.updateItem(item)){
                new Alert(Alert.AlertType.INFORMATION,"Item Updated!").show();
                loadTable();
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

    public void btnCustometFormOnAction(ActionEvent event) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/customer_form.fxml"))));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
