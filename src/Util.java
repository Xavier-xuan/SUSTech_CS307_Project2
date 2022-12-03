import com.google.common.hash.Hashing;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

    public static boolean writeLog(String type, String level, String username, String operation, boolean succeed) throws IOException {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        FileWriter log = new FileWriter(dateFormat.format(date), true);
        String info = "";
        log.write(info);
        log.close();
        return true;
    }

    public static boolean itemExists(String itemName, Connection connection) throws SQLException {
        ResultSet queryResult = connection.createStatement().executeQuery("SELECT  count(*) from item where item.name = '%s'".formatted(itemName));
        queryResult.next();
        return queryResult.getInt("count") > 0;

    }
}
