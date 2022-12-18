package main;

import main.interfaces.*;

import java.sql.*;
import java.util.ArrayList;

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
                String sql ="SELECT name FROM item WHERE (export_city = ? and state = ?) or (import_city = ? and state = ?)";
                allItemStatement = getConnection().prepareStatement(sql);
            }
            String sql = "SELECT * FROM officer WHERE name = ?";
            getPortStatement = getConnection().prepareStatement(sql);
            getPortStatement.setString(1,logInfo.name());
            ResultSet officer = getPortStatement.executeQuery();
            officer.next();
            String city = officer.getString("port_city_name");

            allItemStatement.setString(1, city);
            allItemStatement.setString(2, Util.intToState(3));
            allItemStatement.setString(3, city);
            allItemStatement.setString(4, Util.intToState(8));
            resultSet = allItemStatement.executeQuery();
            ArrayList<String> out = new ArrayList<>();
            while (resultSet.next()) {
                out.add(resultSet.getString("name"));
            }
            String [] Array = new String[out.size()];
            return out.toArray(Array);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
                    if(!Util.getOfficerCity(logInfo.name()).equals(Util.getItemExportCity(itemName))) return false;

                    if (success) {
                        Util.setItemState(itemName, 4, getConnection());
                    } else {
                        Util.setItemState(itemName, 12, getConnection());
                    }
                    Util.setItemExportOfficer(itemName, logInfo.name());
                }
                case 8 -> {
                    if(!Util.getOfficerCity(logInfo.name()).equals(Util.getItemImportCity(itemName))) return false;

                    if (success) {
                        Util.setItemState(itemName, 9, getConnection());
                    } else {
                        Util.setItemState(itemName, 13, getConnection());
                    }
                    Util.setItemImportOfficer(itemName, logInfo.name());
                }
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean login(LogInfo logInfo) {
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
