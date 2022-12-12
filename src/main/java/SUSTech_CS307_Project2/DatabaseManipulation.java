package SUSTech_CS307_Project2;

import SUSTech_CS307_Project2.Loaders.RecordsLoader;
import SUSTech_CS307_Project2.Loaders.StaffsLoader;
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
    public void $import(String recordsCSV, String staffsCSV) {
        try {
            RecordsLoader.loadFromFile(recordsCSV,1000000000);
            StaffsLoader.loadFromFile(staffsCSV,1000000000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
