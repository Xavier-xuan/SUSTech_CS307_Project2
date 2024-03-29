package main;

import main.Loaders.*;
import main.interfaces.*;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;

public class DBManipulation implements IDatabaseManipulation {
    Connection con;
    public static Courier courierDB = new Courier();
    public static SeaportOfficer officerDB = new SeaportOfficer();
    public static CompanyManager managerDB = new CompanyManager();
    public static SustcManager sustcDB = new SustcManager();
    public static String tablePath = "data/records.csv";
    public static String staffPath = "data/staffs.csv";


    public DBManipulation(String database, String root, String pass) {
        ConnectionManager.address = database;
        ConnectionManager.rootUsername = root;
        ConnectionManager.rootPassword = pass;
        ConnectionManager.updateBaseUrl();
        con = ConnectionManager.getRootConnection();
        try {
            new BeforeEnd();
            con.setAutoCommit(false);
            ScriptRunner scriptRunner = new ScriptRunner(con, false, true);
            Reader tables = new InputStreamReader(DBManipulation.class.getResourceAsStream("sql/createTable.sql"));
            Reader users = new InputStreamReader(DBManipulation.class.getResourceAsStream("sql/createUser.sql"));
            scriptRunner.runScript(tables);
            scriptRunner.runScript(users);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void $import(String recordsCSV, String staffsCSV) {
        try {
            RecordsLoader.loadFromString(recordsCSV);
            StaffsLoader.loadFromString(staffsCSV);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void $importFromFile(String recordsCSV, String staffsCSV) {
        try {
            RecordsLoader.loadFromFile(recordsCSV);
            StaffsLoader.loadFromFile(staffsCSV);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public double getImportTaxRate(LogInfo log, String city, String itemClass) {
        return managerDB.getImportTaxRate(log, city, itemClass);
    }

    @Override
    public double getExportTaxRate(LogInfo log, String city, String itemClass) {
        return managerDB.getExportTaxRate(log, city, itemClass);
    }

    @Override
    public boolean loadItemToContainer(LogInfo log, String itemName, String containerCode) {
        return managerDB.loadItemToContainer(log, itemName, containerCode);
    }

    @Override
    public boolean loadContainerToShip(LogInfo log, String shipName, String containerCode) {
        return managerDB.loadContainerToShip(log, shipName, containerCode);
    }

    @Override
    public boolean shipStartSailing(LogInfo log, String shipName) {
        return managerDB.shipStartSailing(log, shipName);
    }

    @Override
    public boolean unloadItem(LogInfo log, String itemName) {
        return managerDB.unloadItem(log, itemName);
    }

    @Override
    public boolean itemWaitForChecking(LogInfo log, String item) {
        return managerDB.itemWaitForChecking(log, item);
    }

    @Override
    public boolean newItem(LogInfo log, ItemInfo item) {
        return courierDB.newItem(log, item);
    }

    @Override
    public boolean setItemState(LogInfo log, String name, ItemState s) {
        return courierDB.setItemState(log, name, s);
    }

    @Override
    public String[] getAllItemsAtPort(LogInfo log) {
        return officerDB.getAllItemsAtPort(log);
    }

    @Override
    public boolean setItemCheckState(LogInfo log, String itemName, boolean success) {
        return officerDB.setItemCheckState(log, itemName, success);
    }

    @Override
    public int getCompanyCount(LogInfo log) {
        return sustcDB.getCompanyCount(log);
    }

    @Override
    public int getCityCount(LogInfo log) {
        return sustcDB.getCityCount(log);
    }

    @Override
    public int getCourierCount(LogInfo log) {
        return sustcDB.getCourierCount(log);
    }

    @Override
    public int getShipCount(LogInfo log) {
        return sustcDB.getShipCount(log);
    }

    @Override
    public ItemInfo getItemInfo(LogInfo log, String name) {
        return sustcDB.getItemInfo(log, name);
    }

    @Override
    public ShipInfo getShipInfo(LogInfo log, String name) {
        return sustcDB.getShipInfo(log, name);
    }

    @Override
    public ContainerInfo getContainerInfo(LogInfo log, String code) {
        return sustcDB.getContainerInfo(log, code);
    }

    @Override
    public StaffInfo getStaffInfo(LogInfo log, String name) {
        return sustcDB.getStaffInfo(log, name);
    }
}

class BeforeEnd {
    BeforeEnd() {
        Thread t = new Thread(() -> {
            try {
                System.out.println("Processing Last Tasks...");
                Connection con = ConnectionManager.getRootConnection();
                Util.dropTableAndUsers(con);
                ConnectionManager.closeAllConnection();
                System.out.println("end...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Runtime.getRuntime().addShutdownHook(t);
    }
}