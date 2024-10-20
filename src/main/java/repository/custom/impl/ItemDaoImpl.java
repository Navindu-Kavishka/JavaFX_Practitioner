package repository.custom.impl;

import dto.Item;
import entity.ItemEntity;
import repository.custom.ItemDao;
import util.CrudUtil;

import java.sql.SQLException;
import java.util.List;

public class ItemDaoImpl implements ItemDao {
    @Override
    public boolean save(ItemEntity item) {
        System.out.println("Item Repository : "+item);
        String SQL = "INSERT INTO item VALUES (?,?,?,?,?)";
        try {
            return CrudUtil.execute(
                    SQL,
                    item.getCode(),
                    item.getDescription(),
                    item.getPackSize(),
                    item.getUnitPrice(),
                    item.getQOH()
            );
        } catch (SQLException e) {
            System.out.println("SQL Error : "+e);
        }
        return false;
    }

    @Override
    public boolean update(ItemEntity entity, String s) {
        return false;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    @Override
    public List<ItemEntity> findAll() {
        return null;
    }

    @Override
    public Item search(String id) {
        return null;
    }
}
