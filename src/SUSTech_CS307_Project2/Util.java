package SUSTech_CS307_Project2;

import com.google.common.hash.Hashing;
import cs307.project2.interfaces.LogInfo;
import spark.Request;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        ResultSet queryResult = connection.createStatement().executeQuery(("SELECT * from item where name = " + itemName));
        return queryResult.next();
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
            case 7 -> state = "Unpacking from Container" ;
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
        String roleText = request.headers("role");
        LogInfo.StaffType role;

        switch (roleText) {
            case "Courier":
                role = LogInfo.StaffType.Courier;
                break;
            case "Company Manager":
                role = LogInfo.StaffType.CompanyManager;
                break;
            case "Seaport Officer":
                role = LogInfo.StaffType.SeaportOfficer;
                break;
            case "SUSTC Manager":
                role = LogInfo.StaffType.SustcManager;
                break;
            default:
                role = null;
        }
        return new LogInfo(username, role, password);
    }
}
