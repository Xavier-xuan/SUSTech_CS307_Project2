package SUSTech_CS307_Project2;

import SUSTech_CS307_Project2.Loaders.RecordsLoader;
import SUSTech_CS307_Project2.Loaders.StaffsLoader;
import cs307.project2.interfaces.IDatabaseManipulation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManipulation implements IDatabaseManipulation {
    Connection con;
    String sql;
    public DatabaseManipulation(String database, String root, String pass) {
        new BeforeEnd();
        ConnectionManager.address = database;
        ConnectionManager.rootUsername = root;
        ConnectionManager.rootPassword = pass;
        ConnectionManager.updateBaseUrl();
        String dataBaseName = "SusTech";
        con = ConnectionManager.getRootConnection();
        try {
            Statement createDB = con.createStatement();
            sql = "CREATE DATABASE " + dataBaseName;
            createDB.execute(sql);
            sql = "\\c "  + dataBaseName;
            createDB.execute(sql);



        } catch (SQLException e) {

        }


    }
    @Override
    public void $import(String recordsCSV, String staffsCSV) {
        try {
            RecordsLoader.loadFromFile(recordsCSV,1000000000);
            StaffsLoader.loadFromFile(staffsCSV,1000000000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}

class BeforeEnd {//程序退出事件处理

    BeforeEnd() {
        //模拟处理时间
        Thread t = new Thread(() -> {
            try {
                //模拟正常终止前任务
                System.out.println("程序即将终止...");
                System.out.println("正在处理最后的事情...");
                Thread.sleep(5 * 1000);//模拟处理时间
                System.out.println("end...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Runtime.getRuntime().addShutdownHook(t);
    }
}