package repository;

import dto.Customer;
import javafx.collections.ObservableList;

import java.util.List;

public interface CrudRepository <T,ID> extends SuperDao{
    boolean save(T entity);
    boolean update(T entity);
    boolean delete(ID id);
    ObservableList<T> findAll();
    Object  search(String id);
    ObservableList<String> getIds();
}
