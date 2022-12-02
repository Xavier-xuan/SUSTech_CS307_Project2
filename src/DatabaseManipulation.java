import cs307.project2.interfaces.*;

public class DatabaseManipulation implements IDatabaseManipulation {
    @Override
    public void $import(String s, String s1) {

    }

    @Override
    public double getImportTaxRate(LogInfo logInfo, String s, String s1) {
        return 0;
    }

    @Override
    public double getExportTaxRate(LogInfo logInfo, String s, String s1) {
        return 0;
    }

    @Override
    public boolean loadItemToContainer(LogInfo logInfo, String s, String s1) {
        return false;
    }

    @Override
    public boolean loadContainerToShip(LogInfo logInfo, String s, String s1) {
        return false;
    }

    @Override
    public boolean shipStartSailing(LogInfo logInfo, String s) {
        return false;
    }

    @Override
    public boolean unloadItem(LogInfo logInfo, String s) {
        return false;
    }

    @Override
    public boolean itemWaitForChecking(LogInfo logInfo, String s) {
        return false;
    }

    @Override
    public boolean newItem(LogInfo logInfo, ItemInfo itemInfo) {
        return false;
    }

    @Override
    public boolean setItemState(LogInfo logInfo, String s, ItemState itemState) {
        return false;
    }

    @Override
    public String[] getAllItemsAtPort(LogInfo logInfo) {
        return new String[0];
    }

    @Override
    public boolean setItemCheckState(LogInfo logInfo, String s, boolean b) {
        return false;
    }

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
}
