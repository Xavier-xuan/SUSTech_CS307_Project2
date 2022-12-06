package SUSTech_CS307_Project2;

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
    public double getImportTaxRate(LogInfo logInfo, String city, String itemClass) {
        if (!login(logInfo)) return -1;
        try {
            if (importTaxRateStatement == null) {
                importTaxRateStatement = getConnection().prepareStatement("SELECT import_tax, price from item where type = ? and import_city = ? limit 1");
            }
            importTaxRateStatement.setString(1, itemClass);
            importTaxRateStatement.setString(2,city);
            ResultSet result = importTaxRateStatement.executeQuery();
            if (result.next()) {
                double price = result.getDouble("price");
                double tax = result.getDouble("import_tax");
                return tax/price;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    @Override
    public double getExportTaxRate(LogInfo logInfo, String city, String itemClass) {
        if (!login(logInfo)) return -1;
        try {
            if (exportTaxRateStatement == null) {
                exportTaxRateStatement = getConnection().prepareStatement("SELECT export_tax, price from item where type = ? and export_city = ? limit 1");
            }
            exportTaxRateStatement.setString(1, itemClass);
            exportTaxRateStatement.setString(2,city);
            ResultSet result = exportTaxRateStatement.executeQuery();
            if (result.next()) {
                double price = result.getDouble("price");
                double tax = result.getDouble("export_tax");
                return tax/price;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    @Override
    public boolean loadItemToContainer(LogInfo logInfo, String itemName, String containerCode) {
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
