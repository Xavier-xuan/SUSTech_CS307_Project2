import cs307.project2.interfaces.ICourier;
import cs307.project2.interfaces.ItemInfo;
import cs307.project2.interfaces.ItemState;
import cs307.project2.interfaces.LogInfo;

public class Courier implements ICourier {
    @Override
    public boolean newItem(LogInfo logInfo, ItemInfo itemInfo) {
        return false;
    }

    @Override
    public boolean setItemState(LogInfo logInfo, String s, ItemState itemState) {
        return false;
    }
}
