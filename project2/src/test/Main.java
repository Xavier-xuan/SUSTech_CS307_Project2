package test;

import main.DatabaseManipulation;
import main.interfaces.LogInfo;

public class Main {
    private static LogInfo logInfo = new LogInfo("Hua Hang", LogInfo.StaffType.SustcManager, "500622842781782190");

    public static void main(String[] args) {
        DatabaseManipulation dm = new DatabaseManipulation("127.0.0.1:5432/postgres", "postgres", "123456");
        dm.$import("./data/records.csv", "./data/staffs.csv");

        dm.getShipInfo(logInfo,"c4fda360");
        boolean tmp = dm.loadItemToContainer(logInfo,"cherry-3a393","c4fda360");
        System.out.print(tmp);
    }
}