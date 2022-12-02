import cs307.project2.interfaces.ISeaportOfficer;
import cs307.project2.interfaces.LogInfo;

public class SeaportOfficer implements ISeaportOfficer {
    @Override
    public String[] getAllItemsAtPort(LogInfo logInfo) {
        return new String[0];
    }

    @Override
    public boolean setItemCheckState(LogInfo logInfo, String s, boolean b) {
        return false;
    }
}
