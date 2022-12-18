package main;

import com.google.common.hash.Hashing;
import main.interfaces.*;
import spark.Request;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static String autoEncryptPassword(String password) {
        if (ConnectionManager.encryptPassword) {
            return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
        } else {
            return password;
        }
    }

    public static boolean writeLog(String level, String username, String operation, boolean succeed) throws IOException {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        FileWriter log = new FileWriter(dateFormat.format(date), true);
        String info = "[%s] [%s] %b | %s %s".formatted(timeFormat.format(date), level, succeed, username, operation);
        log.write(info);
        log.close();
        return true;
    }

    public static boolean itemExists(String itemName, Connection connection) throws SQLException {
        PreparedStatement pres = connection.prepareStatement("SELECT * from item where name = ?");
        pres.setString(1, itemName);
        ResultSet queryResult = pres.executeQuery();
        return queryResult.next();
    }

    public static boolean itemHasContainer(String itemName) throws SQLException {
        Connection connection = ConnectionManager.getDMConnection();
        PreparedStatement pres = connection.prepareStatement("SELECT * from item where name = ? and state = ? and container_code is not null");
        pres.setString(1, itemName);
        pres.setString(2, Util.intToState(4));
        ResultSet queryResult = pres.executeQuery();
        return queryResult.next();
    }

    public static int getItemState(String itemName, Connection connection) throws SQLException {
        PreparedStatement pres = connection.prepareStatement("SELECT * from item where name = ?");
        pres.setString(1, itemName);
        ResultSet queryResult = pres.executeQuery();
        queryResult.next();
        return stateToInt(queryResult.getString("state"));
    }

    public static String getItemCompany(String name) throws SQLException {
        Connection connection = ConnectionManager.getDMConnection();
        PreparedStatement pres = connection.prepareStatement("SELECT * from item where name = ?");
        pres.setString(1, name);
        ResultSet queryResult = pres.executeQuery();
        queryResult.next();
        String courier = queryResult.getString("retrieval_courier");
        return getCourierCompany(courier);
    }

    public static String getShipCompany(String shipName) throws SQLException {
        Connection connection = ConnectionManager.getDMConnection();
        PreparedStatement pres = connection.prepareStatement("SELECT * from ship where name = ?");
        pres.setString(1, shipName);
        ResultSet queryResult = pres.executeQuery();
        queryResult.next();
        return (queryResult.getString("company_name"));
    }

    public static String getCourierCompany(String name) throws SQLException {
        Connection connection = ConnectionManager.getDMConnection();

        PreparedStatement pres = connection.prepareStatement("SELECT * from courier where name = ?");
        pres.setString(1, name);
        ResultSet queryResult = pres.executeQuery();
        queryResult.next();
        return (queryResult.getString("company_name"));
    }

    public static String getCManagerCompany(String name) throws SQLException {
        Connection connection = ConnectionManager.getDMConnection();
        PreparedStatement pres = connection.prepareStatement("SELECT * from company_manager where name = ?");
        pres.setString(1, name);
        ResultSet queryResult = pres.executeQuery();
        queryResult.next();
        return (queryResult.getString("company_name"));
    }

    public static String getOfficerCity(String name) throws SQLException {
        Connection connection = ConnectionManager.getDMConnection();
        PreparedStatement pres = connection.prepareStatement("SELECT * from officer where name = ?");
        pres.setString(1, name);
        ResultSet queryResult = pres.executeQuery();
        queryResult.next();
        return (queryResult.getString("port_city_name"));
    }

    public static String getItemExportCity(String name) throws SQLException {
        Connection connection = ConnectionManager.getDMConnection();
        PreparedStatement pres = connection.prepareStatement("SELECT * from item where name = ?");
        pres.setString(1, name);
        ResultSet queryResult = pres.executeQuery();
        queryResult.next();
        return (queryResult.getString("export_city"));
    }

    public static String getItemImportCity(String name) throws SQLException {
        Connection connection = ConnectionManager.getDMConnection();
        PreparedStatement pres = connection.prepareStatement("SELECT * from item where name = ?");
        pres.setString(1, name);
        ResultSet queryResult = pres.executeQuery();
        queryResult.next();
        return (queryResult.getString("import_city"));
    }

    public static void setItemImportOfficer(String item, String officer) throws SQLException {
        Connection connection = ConnectionManager.getDMConnection();
        PreparedStatement pres = connection.prepareStatement("UPDATE item set import_officer = ? where name = ?");
        pres.setString(1, officer);
        pres.setString(2, item);
        pres.execute();
    }

    public static void setItemExportOfficer(String item, String officer) throws SQLException {
        Connection connection = ConnectionManager.getDMConnection();
        PreparedStatement pres = connection.prepareStatement("UPDATE item set export_officer = ? where name = ?");
        pres.setString(1, officer);
        pres.setString(2, item);
        pres.execute();
    }


    public static boolean shipIsUsing(String name) throws SQLException {
        Connection connection = ConnectionManager.getDMConnection();
        PreparedStatement pres = connection.prepareStatement("SELECT * FROM item WHERE ship_name = ? and ( state = ? )");
        pres.setString(1, name);
        pres.setString(2, Util.intToState(6));
        ResultSet resultSet = pres.executeQuery();
        return resultSet.next();
    }

    public static boolean containerIsUsing(String name) throws SQLException {
        Connection connection = ConnectionManager.getDMConnection();
        PreparedStatement pres = connection.prepareStatement("SELECT * FROM item WHERE container_code = ? and ( state = ? or state = ? or state = ?)");
        pres.setString(1, name);
        pres.setString(2, Util.intToState(5));
        pres.setString(3, Util.intToState(6));
        pres.setString(4, Util.intToState(7));
        ResultSet resultSet = pres.executeQuery();
        return resultSet.next();
    }

    public static boolean setItemState(String itemName, int state, Connection connection) {
        try {
            PreparedStatement setItemStateStatement = connection.prepareStatement("UPDATE item SET state = ? where name = ?");
            Statement check = connection.createStatement();
            setItemStateStatement.setString(2, itemName);
            if (!itemExists(itemName, connection)) return false;
            setItemStateStatement.setString(1, Util.intToState(state));
            return setItemStateStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int stateToInt(String state) {
        int stateInt;
        switch (state) {
            default -> stateInt = 0;
            case "Picking-up" -> stateInt = 1;
            case "To-Export Transporting" -> stateInt = 2;
            case "Export Checking" -> stateInt = 3;
            case "Packing to Container" -> stateInt = 4;
            case "Waiting for Shipping" -> stateInt = 5;
            case "Shipping" -> stateInt = 6;
            case "Unpacking from Container" -> stateInt = 7;
            case "Import Checking" -> stateInt = 8;
            case "From-Import Transporting" -> stateInt = 9;
            case "Delivering" -> stateInt = 10;
            case "Finish" -> stateInt = 11;
            case "Export Check Fail" -> stateInt = 12;
            case "Import Check Fail" -> stateInt = 13;
        }
        return stateInt;
    }

    public static int stateToInt(ItemState state) {
        switch (state) {
            default -> {
                return 0;
            }
            case PickingUp -> {
                return 1;
            }
            case ToExportTransporting -> {
                return 2;
            }
            case ExportChecking -> {
                return 3;
            }
            case PackingToContainer -> {
                return 4;
            }
            case WaitingForShipping -> {
                return 5;
            }
            case Shipping -> {
                return 6;
            }
            case UnpackingFromContainer -> {
                return 7;
            }
            case ImportChecking -> {
                return 8;
            }
            case FromImportTransporting -> {
                return 9;
            }
            case Delivering -> {
                return 10;
            }
            case Finish -> {
                return 11;
            }
            case ExportCheckFailed -> {
                return 12;
            }
            case ImportCheckFailed -> {
                return 13;
            }
        }
    }

    public static ItemState stateTextToObject(String state) {
        switch (state) {

            case "Picking-up" -> {
                return ItemState.PickingUp;
            }
            case "To-Export Transporting" -> {
                return ItemState.ToExportTransporting;
            }
            case "Export Checking" -> {
                return ItemState.ExportChecking;
            }
            case "Packing to Container" -> {
                return ItemState.PackingToContainer;
            }
            case "Waiting for Shipping" -> {
                return ItemState.WaitingForShipping;
            }
            case "Shipping" -> {
                return ItemState.Shipping;
            }
            case "Unpacking from Container" -> {
                return ItemState.UnpackingFromContainer;
            }
            case "Import Checking" -> {
                return ItemState.ImportChecking;
            }
            case "From-Import Transporting" -> {
                return ItemState.FromImportTransporting;
            }
            case "Delivering" -> {
                return ItemState.Delivering;
            }
            case "Finish" -> {
                return ItemState.Finish;
            }
            case "Export Check Fail" -> {
                return ItemState.ExportCheckFailed;
            }
            case "Import Check Fail" -> {
                return ItemState.ImportCheckFailed;
            }
            default -> {
                return null;
            }
        }
    }

    public static String intToState(int stateInt) {
        String state;
        switch (stateInt) {
            default -> state = null;
            case 1 -> state = "Picking-up";
            case 2 -> state = "To-Export Transporting";
            case 3 -> state = "Export Checking";
            case 4 -> state = "Packing to Container";
            case 5 -> state = "Waiting for Shipping";
            case 6 -> state = "Shipping";
            case 7 -> state = "Unpacking from Container";
            case 8 -> state = "Import Checking";
            case 9 -> state = "From-Import Transporting";
            case 10 -> state = "Delivering";
            case 11 -> state = "Finish";
            case 12 -> state = "Export Check Fail";
            case 13 -> state = "Import Check Fail";
        }
        return state;
    }

    public static LogInfo getLogInfo(Request request) {
        String username = request.headers("username");
        String password = request.headers("password");
        String roleText = request.headers("role").toString().trim();
        LogInfo.StaffType role;

        if (roleText == null) {
            return new LogInfo("", LogInfo.StaffType.Courier, "");
        }

        switch (roleText) {
            case "courier":
                role = LogInfo.StaffType.Courier;
                break;
            case "company manager":
                role = LogInfo.StaffType.CompanyManager;
                break;
            case "seaport officer":
                role = LogInfo.StaffType.SeaportOfficer;
                break;
            case "sustc manager":
                role = LogInfo.StaffType.SustcManager;
                break;
            default:
                role = null;
        }
        return new LogInfo(username, role, password);
    }

    public static void dropTableAndUsers(Connection con) throws Exception {
        Statement Statement = con.createStatement();
        con.setAutoCommit(false);
        Statement.executeUpdate("DROP TABLE IF EXISTS city,company,company_manager,container,courier,item,officer,port_city,ship,sustc_manager");
        Statement.executeUpdate("DROP USER IF EXISTS courier,officer,company_manager,sustc_manager;");
        con.commit();
        con.setAutoCommit(true);
        System.out.println("All cleaned");
    }


}