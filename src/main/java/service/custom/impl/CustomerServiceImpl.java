package service.custom.impl;

import entity.CustomerEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import dto.Customer;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.SuperDao;
import repository.custom.CustomerDao;
import service.custom.CustomerService;
import util.DaoType;

import java.util.ArrayList;
import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    CustomerDao dao = DaoFactory.getInstance().getDaoType(DaoType.CUSTOMER);
    @Override
    public boolean addCustomer(Customer customer) {
        System.out.println("Service : "+customer);
        CustomerEntity entity = new ModelMapper().map(customer, CustomerEntity.class);
        return dao.save(entity);
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        return dao.update((new ModelMapper().map(customer, CustomerEntity.class)));
    }

    @Override
    public Customer searchCustomer(String id) {
        return new ModelMapper().map(dao.search(id),Customer.class);
    }

    @Override
    public boolean deleteCustomer(String id) {
        return dao.delete(id);
    }

    @Override
    public ObservableList<Customer> getAllCustomers() {
        List<CustomerEntity> customerEntities = dao.findAll();
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        customerEntities.forEach(customerEntity -> {
            customers.add(new ModelMapper().map(customerEntity,Customer.class));
        });
        return  customers;
    }

    public ObservableList<String> getIds(){
        return dao.getIds();
    }
}
