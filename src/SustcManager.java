import com.google.common.hash.Hashing;
import cs307.project2.interfaces.*;

import java.nio.charset.StandardCharsets;
import java.sql.*;

public class SustcManager implements ISustcManager  {
    private PreparedStatement loginStatement;
    private Connection con;
    @Override
    public int getCompanyCount(LogInfo logInfo) {
        try {
            if (!login(logInfo)) {
                return -1;
            }
            String sql = "select count(*) as cnt from company";
            Statement query = con.createStatement();
            return query.executeQuery(sql).getInt("cnt");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {

        }
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

    private boolean login(LogInfo logInfo) throws SQLException {
        if (logInfo.type() != LogInfo.StaffType.SustcManager) {
            return false;
        }
        if (this.con == null) {
            this.con = getConnection();
        }

        if (this.loginStatement == null) {
            try {
                this.loginStatement = con.prepareStatement("SELECT * FROM sustc_manager WHERE  name = ? AND password = ?");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        String password = Util.autoEncryptPassword(logInfo.password());
        this.loginStatement.setString(1, logInfo.name());
        this.loginStatement.setString(2,password);
        ResultSet result = loginStatement.executeQuery();
        if (result.next()) {
            return true;
        }
        return false;
    }

    private Connection getConnection() {
        return ConnectionManager.getSustcManagerConnection();
    }
}
