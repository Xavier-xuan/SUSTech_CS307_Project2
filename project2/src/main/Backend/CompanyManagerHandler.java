package main.Backend;

import main.*;
import main.interfaces.*;
import spark.Request;
import spark.Response;

public class CompanyManagerHandler {
    static CompanyManager companyManagerInstance;

    /**
     * @url /company_manager/get_import_tax_rate/:city/:item_class
     * @Header username String
     * @Header password String
     * @Header role     String
     * @Method Get
     * @Response String
     * @ResponseExmaple 12.34
     */
    public static String getImportTaxRate(Request request, Response response) {
        LogInfo logInfo = Util.getLogInfo(request);
        String city = request.params(":city");
        String itemClass = request.params(":item_class");

        return String.valueOf(getCompanyManagerInstance().getImportTaxRate(logInfo, city, itemClass));
    }

    /**
     * @url /company_manager/get_export_tax_rate/:city/:item_class
     * @Header username String
     * @Header password String
     * @Header role     String
     * @Method Get
     * @Response String
     * @ResponseExmaple 12.34
     */
    public static String getExportTaxRate(Request request, Response response) {
        LogInfo logInfo = Util.getLogInfo(request);
        String city = request.params(":city");
        String itemClass = request.params(":item_class");

        return String.valueOf(getCompanyManagerInstance().getExportTaxRate(logInfo, city, itemClass));
    }

    /**
     * @url /company_manager/load_item_to_container
     * @Header username String
     * @Header password String
     * @Header role     String
     * @Method Post
     * @RequestParam item_name        String
     * @RequestParam container_code   Code
     * @Response String
     * @ResponseExmaple true|false
     */
    public static String loadItemToContainer(Request request, Response response) {
        LogInfo logInfo = Util.getLogInfo(request);
        String itemName = request.queryParams("item_name");
        String containerCode = request.queryParams("container_code");

        return String.valueOf(getCompanyManagerInstance().loadItemToContainer(logInfo, itemName, containerCode));
    }

    /**
     * @url /company_manager/load_container_to_ship
     * @Header username String
     * @Header password String
     * @Header role     String
     * @Method Post
     * @RequestParam ship_name        String
     * @RequestParam container_code   Code
     * @Response String
     * @ResponseExmaple true|false
     */
    public static String loadContainerToShip(Request request, Response response) {
        LogInfo logInfo = Util.getLogInfo(request);
        String shipName = request.queryParams("ship_name");
        String containerCode = request.queryParams("container_code");

        return String.valueOf(getCompanyManagerInstance().loadContainerToShip(logInfo, shipName, containerCode));

    }

    /**
     * @url /company_manager/ship_start_sailing
     * @Header username String
     * @Header password String
     * @Header role     String
     * @Method Post
     * @RequestParam ship_name        String
     * @Response String
     * @ResponseExmaple true|false
     */
    public static String shipStartSailing(Request request, Response response) {
        LogInfo logInfo = Util.getLogInfo(request);
        String shipName = request.queryParams("ship_name");

        return String.valueOf(getCompanyManagerInstance().shipStartSailing(logInfo, shipName));
    }

    /**
     * @url /company_manager/ship_start_sailing
     * @Header username String
     * @Header password String
     * @Header role     String
     * @Method Post
     * @RequestParam item_name        String
     * @Response String
     * @ResponseExmaple true|false
     */
    public static String unloadItem(Request request, Response response) {
        LogInfo logInfo = Util.getLogInfo(request);
        String itemName = request.queryParams("item_name");
        return String.valueOf(getCompanyManagerInstance().unloadItem(logInfo, itemName));
    }

    /**
     * @url /company_manager/ship_start_sailing
     * @Header username String
     * @Header password String
     * @Header role     String
     * @Method Post
     * @RequestParam item_name        String
     * @Response String
     * @ResponseExmaple true|false
     */
    public static String itemWaitForChecking(Request request, Response response) {
        LogInfo logInfo = Util.getLogInfo(request);
        String itemName = request.params("item_name");
        return String.valueOf(getCompanyManagerInstance().itemWaitForChecking(logInfo, itemName));
    }

    public static CompanyManager getCompanyManagerInstance() {
        if (companyManagerInstance == null) {
            companyManagerInstance = new CompanyManager();
        }

        return companyManagerInstance;
    }
}
