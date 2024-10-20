package repository.custom.impl;

import dto.Customer;
import entity.CustomerEntity;
import repository.DaoFactory;
import repository.SuperDao;
import repository.custom.CustomerDao;
import util.CrudUtil;
import util.DaoType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {
    @Override
    public boolean save(CustomerEntity customer) {
        String SQL = "INSERT INTO customer VALUES(?,?,?,?,?,?,?,?,?)";
        try {
            return CrudUtil.execute(
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
        } catch (SQLException e) {
            System.out.println("SQL Error : "+e);
        }
        return false;
    }

    @Override
    public boolean update(CustomerEntity entity, String s) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        String SQL = "DELETE FROM customer WHERE CustID=?";
        try {
            return CrudUtil.execute(SQL,id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CustomerEntity> findAll() {
        return null;
    }

    public Customer search(String id) {
        String SQL = "SELECT * FROM customer WHERE CustID=?";
        try {
            ResultSet resultSet = CrudUtil.execute(SQL,id);
            resultSet.next();
            return new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDate(4).toLocalDate(),
                    resultSet.getDouble(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8),
                    resultSet.getString(9)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
