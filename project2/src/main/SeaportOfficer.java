package main;

import main.interfaces.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SeaportOfficer implements ISeaportOfficer {
    PreparedStatement loginStatement;
    PreparedStatement getPortStatement;
    PreparedStatement allItemStatement;
    @Override
    public String[] getAllItemsAtPort(LogInfo logInfo) {
        if (!login(logInfo)) return new String[0];
        ResultSet resultSet;
        try {
            if (allItemStatement == null) {
                String sql ="SELECT name FROM item WHERE export_city = ?";
                allItemStatement = getConnection().prepareStatement(sql);
            }
            String sql = "SELECT * FROM officer WHERE name = ?";
            getPortStatement = getConnection().prepareStatement(sql);
            getPortStatement.setString(1,logInfo.name());
            ResultSet officer = getPortStatement.executeQuery();
            officer.next();
            String city = officer.getString("port_city_name");
            allItemStatement.setString(1, city);
            resultSet = allItemStatement.executeQuery();
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
            if (!Util.itemExists(itemName,getConnection())) return false;
            int state = Util.getItemState(itemName, getConnection());
            switch (state) {
                default -> {
                    return false;
                }
                case 3 -> {
                    if (success) {
                        return Util.setItemState(itemName, 4, getConnection());
                    } else {
                        return Util.setItemState(itemName, 12, getConnection());
                    }
                }
                case 8 -> {
                    if (success) {
                        return Util.setItemState(itemName, 9, getConnection());
                    } else {
                        return Util.setItemState(itemName, 13, getConnection());
                    }
                }
            }
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
