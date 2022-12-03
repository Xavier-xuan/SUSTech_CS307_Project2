import com.google.common.hash.Hashing;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
}
