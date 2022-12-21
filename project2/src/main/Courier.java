package main;

import main.interfaces.*;

import java.sql.*;

public class Courier implements ICourier {
    PreparedStatement loginStatement;
    PreparedStatement queryItemStatement;
    PreparedStatement insertCityStatement;
    PreparedStatement insertPortCityStatement;
    PreparedStatement insertItemStatement;
    PreparedStatement setDeliveryCourierStatement;
    PreparedStatement setItemStateStatement;


    @Override
    public boolean newItem(LogInfo logInfo, ItemInfo itemInfo) {
        if (!login(logInfo)) return false;

        try {
            // check illegal field
            if (itemInfo.name() == null || itemInfo.$class() == null || itemInfo.price() < 0 ||
                    itemInfo.retrieval() == null || itemInfo.retrieval().city() == null ||
                    itemInfo.delivery() == null || itemInfo.delivery().city() == null || itemInfo.export() == null || itemInfo.export().city() == null ||
                    itemInfo.export().tax() < 0 || itemInfo.$import().city() == null || itemInfo.$import().tax() < 0)
                return false;

            // courier is current user
            if (itemInfo.retrieval().courier() != null && !itemInfo.retrieval().courier().equals(logInfo.name())) {
                return false;
            }

            // state must be null
            if (itemInfo.state() != null) {
                return false;
            }

            // check whether item exists
            if (Util.itemExists(itemInfo.name(), getConnection())) return false;

            // check whether the export city is the same
            if (itemInfo.export().city().equals(itemInfo.$import().city()) || itemInfo.retrieval().city().equals(itemInfo.delivery().city())) {
                return false;
            }

            // check whether the tax rate is correct
            double exportTaxRate = Util.getExportTaxRate(getConnection(), itemInfo.export().city(), itemInfo.$class());
            double importTaxRate = Util.getImportTaxRate(getConnection(), itemInfo.$import().city(), itemInfo.$class());
            double actualExportTaxRate = itemInfo.export().tax() / itemInfo.price();
            double actualImportTaxRate = itemInfo.$import().tax() / itemInfo.price();
            if (exportTaxRate != -1 && Math.abs(exportTaxRate - actualExportTaxRate) > 0.0001) return false;
            if (importTaxRate != -1 && Math.abs(importTaxRate - actualImportTaxRate) > 0.0001) return false;


            // check if the retrieval city is where the courier works
            Statement queryWorkingCity = getConnection().createStatement();
            ResultSet queryResult = queryWorkingCity.executeQuery("SELECT city_name from courier where courier.name = '%s'".formatted(logInfo.name()));
            if (queryResult.next()) {
                String workingCity = queryResult.getString("city_name");
                if (!workingCity.equals(itemInfo.retrieval().city())) {
                    return false;
                }
            } else {
                return false;
            }

            // insert city and port city if not exist;
            if (insertCityStatement == null)
                insertCityStatement = getConnection().prepareStatement("INSERT INTO city (name) VALUES (?) ON CONFLICT DO NOTHING ");

            if (insertPortCityStatement == null)
                insertPortCityStatement = getConnection().prepareStatement("INSERT INTO port_city(name) values (?) ON CONFLICT DO NOTHING ");

            insertCityStatement.setString(1, itemInfo.retrieval().city());
            insertCityStatement.execute();
            insertCityStatement.setString(1, itemInfo.delivery().city());
            insertCityStatement.execute();

            insertPortCityStatement.setString(1, itemInfo.export().city());
            insertPortCityStatement.execute();
            insertPortCityStatement.setString(1, itemInfo.retrieval().city());
            insertPortCityStatement.execute();

            // insert item
            if (insertItemStatement == null)
                insertItemStatement = getConnection().prepareStatement("" +
                        "INSERT INTO item(name, price, type, export_tax, import_tax, export_city, import_city, from_city_name, to_city_name, retrieval_courier, state) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            insertItemStatement.setString(1, itemInfo.name());
            insertItemStatement.setDouble(2, itemInfo.price());
            insertItemStatement.setString(3, itemInfo.$class());
            insertItemStatement.setDouble(4, itemInfo.export().tax());
            insertItemStatement.setDouble(5, itemInfo.$import().tax());
            insertItemStatement.setString(6, itemInfo.export().city());
            insertItemStatement.setString(7, itemInfo.$import().city());
            insertItemStatement.setString(8, itemInfo.retrieval().city());
            insertItemStatement.setString(9, itemInfo.delivery().city());
            insertItemStatement.setString(10, logInfo.name());
            insertItemStatement.setString(11, "Picking-up");
            return insertItemStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean setItemState(LogInfo logInfo, String s, ItemState itemState) {
        if (!login(logInfo)) return false;

        try {
            if (queryItemStatement == null)
                queryItemStatement = getConnection().prepareStatement("SELECT * FROM  item where item.name = ?");
            queryItemStatement.setString(1, s);
            ResultSet queryResult = queryItemStatement.executeQuery();

            // check whether item exists
            if (!queryResult.next()) return false;

            // check whether the courier has permission
            String stateText;
            int currentStateNumber = Util.stateToInt(queryResult.getString("state"));
            int targetStateNumber = Util.stateToInt(itemState);
            if (0 < currentStateNumber && currentStateNumber <= 2) {
                // only can be set to its next state
                if (targetStateNumber - currentStateNumber != 1) return false;

                // if is not current courier
                if (queryResult.getString("retrieval_courier") == null || !queryResult.getString("retrieval_courier").equals(logInfo.name()))
                    return false;


            } else if (currentStateNumber == 9 && targetStateNumber == 9) { // From-Import Transporting to From-Import Transporting
                if (queryResult.getString("delivery_courier") != null) {
                    return false;
                }

                // check if the delivery city is where the courier works
                Statement queryWorkingCity = getConnection().createStatement();
                ResultSet cityResult = queryWorkingCity.executeQuery("SELECT city_name from courier where courier.name = '%s'".formatted(logInfo.name()));
                if (cityResult.next()) {
                    String workingCity = cityResult.getString("city_name");
                    if (!workingCity.equals(queryResult.getString("to_city_name"))) {
                        return false;
                    }
                } else {
                    return false;
                }

                // update courier
                if (setDeliveryCourierStatement == null)
                    setDeliveryCourierStatement = getConnection().prepareStatement("UPDATE item SET delivery_courier = ? WHERE  item.name = ?");
                setDeliveryCourierStatement.setString(1, logInfo.name());
                setDeliveryCourierStatement.setString(2, s);
                return setDeliveryCourierStatement.executeUpdate() > 0;

            } else if (9 <= currentStateNumber && currentStateNumber <= 10) {
                // only can be set to its next state
                if (targetStateNumber - currentStateNumber != 1) return false;

                // if is not current courier
                if (!queryResult.getString("delivery_courier").equals(logInfo.name()))
                    return false;
            } else {
                return false;
            }

            // update state normally
            if (setItemStateStatement == null)
                setItemStateStatement = getConnection().prepareStatement("UPDATE item SET state = ? where item.name = ?");
            setItemStateStatement.setString(1, Util.intToState(targetStateNumber));
            setItemStateStatement.setString(2, s);
            return setItemStateStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean login(LogInfo logInfo) {
        if (logInfo.type() != LogInfo.StaffType.Courier) {
            return false;
        }
        String password = Util.autoEncryptPassword(logInfo.password());
        try {
            if (loginStatement == null) {
                loginStatement = getConnection().prepareStatement("SELECT * FROM courier WHERE  name = ? AND password = ?");
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
        return ConnectionManager.getCourierConnection();
    }
}
