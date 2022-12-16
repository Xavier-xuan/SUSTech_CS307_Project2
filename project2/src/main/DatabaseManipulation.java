package main;

import main.Loaders.*;
import main.interfaces.*;

import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;

public class DatabaseManipulation implements IDatabaseManipulation {
    Connection con;
    public static Courier courierDB = new Courier();
    public static SeaportOfficer officerDB = new SeaportOfficer();
    public static CompanyManager managerDB = new CompanyManager();
    public static SustcManager sustcDB = new SustcManager();

    public DatabaseManipulation(String database, String root, String pass) {
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

            scriptRunner.setDelimiter(";;;;", false);
            scriptRunner.runScript(users);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void $import(String recordsCSV, String staffsCSV) {
        try {
            RecordsLoader.loadFromFile(recordsCSV);
            StaffsLoader.loadFromFile(staffsCSV);
//            NormalLoader.loadFromFile(recordsCSV, 10);
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