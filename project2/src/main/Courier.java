package main;

import main.interfaces.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                    itemInfo.retrieval() == null || itemInfo.retrieval().courier() == logInfo.name() || itemInfo.retrieval().city() == null ||
                    itemInfo.delivery() == null || itemInfo.delivery().city() == null || itemInfo.export() == null || itemInfo.export().city() == null ||
                    itemInfo.export().tax() < 0 || itemInfo.$import().city() == null || itemInfo.$import().tax() < 0)
                return false;

            if (itemInfo.state() != null && itemInfo.state() != ItemState.PickingUp){
                return false;
            }

            // check whether item exists
            if (Util.itemExists(itemInfo.name(), getConnection())) return false;

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
                        "INSERT INTO item(name, price, type, export_tax, import_tax, export_city, import_city, from_city_name, to_city_name, delivery_courier, state) " +
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
            insertItemStatement.setString(10, itemInfo.retrieval().courier());
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
            switch (itemState) {
                case ToExportTransporting:
                    if ((!queryResult.getString("state").equals("Picking-up")) ||
                            (!queryResult.getString("retrieval_courier").equals(logInfo.name())))
                        return false;

                    stateText = "To-Export Transporting";
                    break;

                case ExportChecking:
                    if ((!queryResult.getString("state").equals("Picking-up")) ||
                            (!queryResult.getString("state").equals("To-Export Transporting")) ||
                            (!queryResult.getString("retrieval_courier").equals(logInfo.name())))
                        return false;
                    stateText = "Export Checking";

                    break;

                case FromImportTransporting:
                    if (!queryResult.getString("state").equals("From-Import Transporting"))
                        return false;
                    else {
                        if (setDeliveryCourierStatement == null)
                            setDeliveryCourierStatement = getConnection().prepareStatement("UPDATE item SET delivery_courier = ? WHERE  item.name = ?");
                        setDeliveryCourierStatement.setString(1, logInfo.name());
                        setDeliveryCourierStatement.setString(2, s);
                        return setDeliveryCourierStatement.executeUpdate() > 0;
                    }
                case Delivering:
                    if ((!queryResult.getString("state").equals("From-Import Transporting"))
                            || (!queryResult.getString("delivery_courier").equals(logInfo.name())))
                        return false;
                    stateText = "Delivering";
                    break;

                case Finish:
                    if ((!queryResult.getString("state").equals("From-Import Transporting")) ||
                            (!queryResult.getString("state").equals("Delivering")) ||
                            (!queryResult.getString("delivery_courier").equals(logInfo.name())))
                        return false;
                    stateText = "Finish";
                    break;
                default:
                    return false;
            }

            if (setItemStateStatement == null)
                setItemStateStatement = getConnection().prepareStatement("UPDATE item SET state = ? where item.name = ?");
            setItemStateStatement.setString(1, stateText);
            setItemStateStatement.setString(2, s);
            return setItemStateStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean login(LogInfo logInfo) {
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
