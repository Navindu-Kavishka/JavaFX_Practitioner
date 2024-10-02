package util;

import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CrudUtil {
    public static <T>T execute(String sql,Object... args) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);

        for (int i=0;i< args.length;i++){
            pstm.setObject(i+1,args[i]);
        }

        if (sql.startsWith("SELECT") || sql.startsWith("select")){
            return (T) pstm.executeQuery();
        }
        return (T) (Boolean)(pstm.executeUpdate()>0);
    }

    
    
    
//    public void sum(Integer... numbers){
//        for (int i=0;i<numbers.length;i++){
//            System.out.println(numbers[i]);
//        }
//    }
//
//    CrudUtil(){
//        sum(10,20,30,34,45,56);
//        sum(21,32,2,45,66,43,32,53,24,56);
//    }

}
