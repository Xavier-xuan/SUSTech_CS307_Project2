package SUSTech_CS307_Project2.Backend;

import SUSTech_CS307_Project2.CompanyManager;
import SUSTech_CS307_Project2.Util;
import cs307.project2.interfaces.LogInfo;
import spark.Request;
import spark.Response;

public class CompanyManagerHandler {
    static CompanyManager companyManagerInstance;

    public static String getImportTaxRate(Request request, Response response) {
        LogInfo logInfo = Util.getLogInfo(request);
        String city = request.queryParams(":city");
        String itemClass = request.queryParams(":item_class");

        return String.valueOf(getCompanyManagerInstance().getImportTaxRate(logInfo, city, itemClass));
    }

    public static String getExportTaxRate(Request request, Response response) {
        LogInfo logInfo = Util.getLogInfo(request);
        String city = request.queryParams(":city");
        String itemClass = request.queryParams(":item_class");

        return String.valueOf(getCompanyManagerInstance().getExportTaxRate(logInfo, city, itemClass));
    }

    public static CompanyManager getCompanyManagerInstance() {
        if (companyManagerInstance == null) {
            companyManagerInstance = new CompanyManager();
        }

        return companyManagerInstance;
    }
}
