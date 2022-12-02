import cs307.project2.interfaces.ICompanyManager;
import cs307.project2.interfaces.LogInfo;

public class CompanyManager implements ICompanyManager {
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
}
