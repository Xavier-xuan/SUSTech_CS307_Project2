import cs307.project2.interfaces.*;

import java.sql.Connection;

public class SustcManager implements ISustcManager {
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

    private Connection getConnection() {
        return ConnectionManager.getSustcManagerConnection();
    }
}
