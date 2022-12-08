package SUSTech_CS307_Project2;

import cs307.project2.interfaces.ISeaportOfficer;
import cs307.project2.interfaces.LogInfo;

import java.sql.*;

public class SeaportOfficer implements ISeaportOfficer {
    PreparedStatement loginStatement;
    PreparedStatement setItemStateStatement;
    Statement allItemStatement;

    @Override
    public String[] getAllItemsAtPort(LogInfo logInfo) {
        if (!login(logInfo)) return new String[0];
        ResultSet resultSet;
        try {
            if (allItemStatement == null) {
                    allItemStatement = getConnection().createStatement();
            }
            resultSet = allItemStatement.executeQuery("SELECT name FROM item");
            if (resultSet.next()) {
                return (String[]) resultSet.getArray("name").getArray();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new String[0];
    }

    @Override
    public boolean setItemCheckState(LogInfo logInfo, String itemName, boolean success) {
        if (!login(logInfo)) return false;
        try {
            Statement check = getConnection().createStatement();
            ResultSet rel = check.executeQuery(("SELECT state from item where name = " + itemName));
            if (setItemStateStatement==null) {
                setItemStateStatement = getConnection().prepareStatement("UPDATE item SET state = ? where name = ?");
            }
            setItemStateStatement.setString(2,itemName);

            if (!rel.next()) return false;
            int state = Util.stateToInt(rel.getString("state"));
            switch (state) {
                default -> {
                    return false;
                }
                case 3 -> {
                    if (success) {
                        setItemStateStatement.setString(1, Util.intToState(4));
                    } else {
                        setItemStateStatement.setString(1, Util.intToState(12));
                    }
                }
                case 8 -> {
                    if (success) {
                        setItemStateStatement.setString(1, Util.intToState(9));
                    } else {
                        setItemStateStatement.setString(1, Util.intToState(13));
                    }
                }
            }
            return setItemStateStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean login(LogInfo logInfo) {
        if (logInfo.type() != LogInfo.StaffType.SeaportOfficer) {
            return false;
        }
        String password = Util.autoEncryptPassword(logInfo.password());
        try {
            if (loginStatement == null) {
                loginStatement = getConnection().prepareStatement("SELECT * FROM officer WHERE  name = ? AND password = ?");
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
        return ConnectionManager.getOfficerConnection();
    }
}
