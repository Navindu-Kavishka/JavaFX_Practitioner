package controller.customer;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
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
import model.Customer;
import util.CrudUtil;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
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

        String SQL = "INSERT INTO customer VALUES(?,?,?,?,?,?,?,?,?)";

        try {
           boolean isCustomerAdd = CrudUtil.execute(
                   SQL,
                   customer.getId(),
                   customer.getTitle(),
                   customer.getName(),
                   customer.getDob(),
                   customer.getSalary(),
                   customer.getAddress(),
                   customer.getCity(),
                   customer.getProvince(),
                   customer.getPostalCode()
           );
           if (isCustomerAdd){
               new Alert(Alert.AlertType.INFORMATION,"Customer Added :)").show();
               loadTable();
           }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Customer Not Added :(").show();
        }

        System.out.println(customer);

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String SQL = "DELETE FROM customer WHERE CustID=?";
        try {
            Boolean isDeleted = CrudUtil.execute(SQL,txtCustomerId.getText());

            if (isDeleted){
                new Alert(Alert.AlertType.INFORMATION,""+txtCustomerId.getText()+": Customer Deleted !!").show();
                loadTable();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.INFORMATION,""+txtCustomerId.getText()+": Customer not Deleted !!").show();
        }
    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {

    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String SQL = "SELECT * FROM customer WHERE CustID=?";
        try {

            ResultSet resultSet = CrudUtil.execute(SQL,txtCustomerId.getText());
            resultSet.next();
            setValueToText(new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDate(4).toLocalDate(),
                    resultSet.getDouble(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8),
                    resultSet.getString(9)
            ));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

        String SQl = "Update customer SET CustName=?, CustTitle=?, DOB=?,  salary=?,  CustAddress=?, City=?, Province=?, PostalCode=? WHERE CustID=?";
        try {

            Boolean isUpdated = CrudUtil.execute(
                    SQl,
                    customer.getName(),
                    customer.getTitle(),
                    customer.getDob(),
                    customer.getSalary(),
                    customer.getAddress(),
                    customer.getCity(),
                    customer.getProvince(),
                    customer.getPostalCode(),
                    customer.getId()
            );

            if (isUpdated){
                new Alert(Alert.AlertType.INFORMATION,"Customer Updated!").show();
                loadTable();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Customer Not Updated!").show();
            throw new RuntimeException(e);
        }
    }

    private void loadTable() {
        ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        colProvince.setCellValueFactory(new PropertyValueFactory<>("province"));
        try {

            String SQL = "SELECT * FROM customer";

            ResultSet resultSet = CrudUtil.execute(SQL);

            while (resultSet.next()) {
                Customer customer = new Customer(
                        resultSet.getString("CustID"),
                        resultSet.getString("CustTitle"),
                        resultSet.getString("CustName"),
                        resultSet.getDate("dob").toLocalDate(),
                        resultSet.getDouble("salary"),
                        resultSet.getString("CustAddress"),
                        resultSet.getString("city"),
                        resultSet.getString("province"),
                        resultSet.getString("postalCode")
                );
                customerObservableList.add(customer);
            }

            tblCustomers.setItems(customerObservableList);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
