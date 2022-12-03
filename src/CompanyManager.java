import cs307.project2.interfaces.ICompanyManager;
import cs307.project2.interfaces.LogInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompanyManager implements ICompanyManager {
    PreparedStatement loginStatement;
    PreparedStatement importTaxRateStatement;
    PreparedStatement exportTaxRateStatement;
    PreparedStatement loadItemStatement;
    PreparedStatement loadContainerStatement;
    PreparedStatement startSailingStatement;
    PreparedStatement unloadItemStatement;
    PreparedStatement setItemWaitForCheckingStatement;

    @Override
    public double getImportTaxRate(LogInfo logInfo, String s, String s1) {
        if (!login(logInfo)) return -1;

        return 0;
    }

    @Override
    public double getExportTaxRate(LogInfo logInfo, String s, String s1) {
        if (!login(logInfo)) return -1;

        return 0;
    }

    @Override
    public boolean loadItemToContainer(LogInfo logInfo, String s, String s1) {
        if (!login(logInfo)) return false;

        return false;
    }

    @Override
    public boolean loadContainerToShip(LogInfo logInfo, String s, String s1) {
        if (!login(logInfo)) return false;

        return false;
    }

    @Override
    public boolean shipStartSailing(LogInfo logInfo, String s) {
        if (!login(logInfo)) return false;


        return false;
    }

    @Override
    public boolean unloadItem(LogInfo logInfo, String s) {
        if (!login(logInfo)) return false;


        return false;
    }

    @Override
    public boolean itemWaitForChecking(LogInfo logInfo, String s) {
        if (!login(logInfo)) return false;


        return false;
    }

    private boolean login(LogInfo logInfo) {
        if (logInfo.type() != LogInfo.StaffType.CompanyManager) {
            return false;
        }

        String password = Util.autoEncryptPassword(logInfo.password());
        try {
            if (loginStatement == null) {
                loginStatement = getConnection().prepareStatement("SELECT * FROM company_manager WHERE  name = ? AND password = ?");
            }

            loginStatement.setString(1, logInfo.name());
            loginStatement.setString(2, password);

            ResultSet result = loginStatement.executeQuery();
            return result.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection getConnection() {
        return ConnectionManager.getCompanyManagerConnection();
    }

}
