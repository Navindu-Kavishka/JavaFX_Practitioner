package controller.customer;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import dto.Customer;
import service.ServiceFactory;
import util.ServiceType;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {

    @FXML
    private TableColumn<?, ?> colDob;

    @FXML
    private JFXComboBox <String> cmbTitle;

    @FXML
    private TableColumn colAddress;

    @FXML
    private TableColumn colCity;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colName;

    @FXML
    private TableColumn colPostalCode;

    @FXML
    private TableColumn colProvince;

    @FXML
    private TableColumn colSalary;

    @FXML
    private TableColumn colTitle;

    @FXML
    private DatePicker dateDob;

    @FXML
    private TableView<Customer> tblCustomers;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtCity;

    @FXML
    private JFXTextField txtCustomerId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtPostalCode;

    @FXML
    private JFXTextField txtProvince;

    @FXML
    private JFXTextField txtSalary;

    CustomerService1 customerController = new CustomerController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> customerTitleList = FXCollections.observableArrayList();
        customerTitleList.add("Mr");
        customerTitleList.add("Mrs");
        customerTitleList.add("Miss");
        customerTitleList.add("Ms");
        cmbTitle.setItems(customerTitleList);
        loadTable();

        tblCustomers.getSelectionModel().selectedItemProperty().addListener((observableValue, customer, newValue) -> {
            if (newValue!=null){
                setValueToText(newValue);
            }
        });
    }
    private void setValueToText(Customer newValue) {
        txtCustomerId.setText(newValue.getId());
        txtName.setText(newValue.getName());
        cmbTitle.setValue(newValue.getTitle());
        txtProvince.setText(newValue.getProvince());
        txtPostalCode.setText(newValue.getPostalCode());
        txtSalary.setText(newValue.getSalary().toString());
        dateDob.setValue(newValue.getDob());
        txtAddress.setText(newValue.getAddress());
        txtCity.setText(newValue.getCity());
    }
    @FXML
    void btnAddOnAction(ActionEvent event) {
        Customer customer = new Customer(
                txtCustomerId.getText(),
                cmbTitle.getValue(),
                txtName.getText(),
                dateDob.getValue(),
                Double.parseDouble(txtSalary.getText()),
                txtAddress.getText(),
                txtCity.getText(),
                txtProvince.getText(),
                txtPostalCode.getText()
        );
        service.custom.CustomerService service = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);

        if (service.addCustomer(customer)){
            new Alert(Alert.AlertType.INFORMATION,"Customer Added ! ").show();
            loadTable();
        }else {
            new Alert(Alert.AlertType.ERROR,"Customer not Added ! ").show();
        }

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        service.custom.CustomerService service = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);
        if (service.deleteCustomer(txtCustomerId.getText())){
            new Alert(Alert.AlertType.INFORMATION,""+txtCustomerId.getText()+": Customer Deleted !!").show();
            loadTable();
        }else {
            new Alert(Alert.AlertType.INFORMATION, "" + txtCustomerId.getText() + ": Customer not Deleted !!").show();
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        service.custom.CustomerService service = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);
        setValueToText(service.searchCustomer(txtCustomerId.getText()));
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

        Customer customer = new Customer(
                txtCustomerId.getText(),
                cmbTitle.getValue(),
                txtName.getText(),
                dateDob.getValue(),
                Double.parseDouble(txtSalary.getText()),
                txtAddress.getText(),
                txtCity.getText(),
                txtProvince.getText(),
                txtPostalCode.getText()
        );

        service.custom.CustomerService service = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);

        if (service.updateCustomer(customer)){
            new Alert(Alert.AlertType.INFORMATION,"Customer Updated!").show();
            loadTable();
        }
        else {
            new Alert(Alert.AlertType.ERROR, "Customer Not Updated!").show();
        }
    }

    private void loadTable() {
        //ObservableList<Customer> customers = customerController.getAllCustomers();

        service.custom.CustomerService service = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);
        ObservableList<Customer> customers = service.getAllCustomers();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        colProvince.setCellValueFactory(new PropertyValueFactory<>("province"));

        tblCustomers.setItems(customers);
    }


}
