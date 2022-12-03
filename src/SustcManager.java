import com.google.common.hash.Hashing;
import cs307.project2.interfaces.*;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SustcManager implements ISustcManager {
    Statement loginStatement;

    @Override
    public int getCompanyCount(LogInfo logInfo) {
        return 0;
    }

    @Override
    public int getCityCount(LogInfo logInfo) {
        return 0;
    }

    @Override
    public int getCourierCount(LogInfo logInfo) {
        return 0;
    }

    @Override
    public int getShipCount(LogInfo logInfo) {
        return 0;
    }

    @Override
    public ItemInfo getItemInfo(LogInfo logInfo, String s) {
        return null;
    }

    @Override
    public ShipInfo getShipInfo(LogInfo logInfo, String s) {
        return null;
    }

    @Override
    public ContainerInfo getContainerInfo(LogInfo logInfo, String s) {
        return null;
    }

    @Override
    public StaffInfo getStaffInfo(LogInfo logInfo, String s) {
        return null;
    }

    private boolean login(LogInfo logInfo) {
        if (logInfo.type() != LogInfo.StaffType.SustcManager) {
            return false;
        }

        if (loginStatement == null) {
            try {
                loginStatement = getConnection().prepareStatement("SELECT * FROM sustc_manager WHERE  name = ? AND password = ?");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        String password = Util.autoEncryptPassword(logInfo.password());

        return false;
    }

    private Connection getConnection() {
        return ConnectionManager.getSustcManagerConnection();
    }
}
