package service.custom.impl;

import entity.CustomerEntity;
import javafx.collections.ObservableList;
import dto.Customer;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.SuperDao;
import repository.custom.CustomerDao;
import service.custom.CustomerService;
import util.DaoType;

public class CustomerServiceImpl implements CustomerService {
    @Override
    public boolean addCustomer(Customer customer) {
        System.out.println("Service : "+customer);
        CustomerDao customerDao = DaoFactory.getInstance().getDaoType(DaoType.CUSTOMER);
        CustomerEntity entity = new ModelMapper().map(customer, CustomerEntity.class);
        return customerDao.save(entity);
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        CustomerDao dao = DaoFactory.getInstance().getDaoType(DaoType.CUSTOMER);
        return dao.update((new ModelMapper().map(customer, CustomerEntity.class)));
    }

    @Override
    public Customer searchCustomer(String id) {
        CustomerDao dao = DaoFactory.getInstance().getDaoType(DaoType.CUSTOMER);
        return new ModelMapper().map(dao.search(id),Customer.class);
    }

    @Override
    public boolean deleteCustomer(String id) {
        System.out.println("Service delete"+id);
        CustomerDao dao = DaoFactory.getInstance().getDaoType(DaoType.CUSTOMER);
        return dao.delete(id);
    }

    @Override
    public ObservableList<Customer> getAllCustomers() {
        return null;
    }
}
