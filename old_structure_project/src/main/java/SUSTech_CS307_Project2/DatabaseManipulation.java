package SUSTech_CS307_Project2;

import SUSTech_CS307_Project2.ConnectionManager;
import SUSTech_CS307_Project2.Loaders.RecordsLoader;
import SUSTech_CS307_Project2.Loaders.ScriptRunner;
import SUSTech_CS307_Project2.Loaders.StaffsLoader;
import SUSTech_CS307_Project2.Util;
import cs307.project2.interfaces.IDatabaseManipulation;

import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseManipulation implements IDatabaseManipulation {
    Connection con;
    public static Courier courierDB = new Courier();
    public static SeaportOfficer officerDB = new SeaportOfficer();
    public static CompanyManager managerDB = new CompanyManager();
    public static SustcManager sustcDB = new SustcManager();;
    String sql;
    public static String dataBaseName = "project2";
    public DatabaseManipulation(String database, String root, String pass) {
//        new BeforeEnd();
        ConnectionManager.address = database;
        ConnectionManager.rootUsername = root;
        ConnectionManager.rootPassword = pass;
        ConnectionManager.updateBaseUrl();
        con = ConnectionManager.getRootConnection();
        try {
            con.setAutoCommit(false);
            ScriptRunner scriptRunner = new ScriptRunner(con, false, true);
            Reader tables = new FileReader("sql/createTable.sql");
            Reader users = new FileReader("sql/createUser.sql");
            scriptRunner.runScript(tables);
            scriptRunner.runScript(users);













        } catch (Exception e) {
            throw new RuntimeException(e);
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
                Connection con = ConnectionManager.getRootConnection();
                Util.dropTables(con);
                System.out.println("end...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Runtime.getRuntime().addShutdownHook(t);
    }
}