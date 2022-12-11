package SUSTech_CS307_Project2;

import cs307.project2.interfaces.IDatabaseManipulation;

import java.sql.Connection;

public class DatabaseManipulation implements IDatabaseManipulation {
    Connection con;
    String sql;
    public DatabaseManipulation(String database, String root, String pass) {
        con = ConnectionManager.getRootConnection();
        sql = "";

        ConnectionManager.updateBaseUrl();
    }
    @Override
    public void $import(String s, String s1) {

    }

}
