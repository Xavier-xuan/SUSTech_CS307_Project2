package SUSTech_CS307_Project2.Backend;

import SUSTech_CS307_Project2.SustcManager;
import SUSTech_CS307_Project2.Util;
import com.google.gson.Gson;
import cs307.project2.interfaces.*;
import spark.Request;
import spark.Response;

public class SustcManagerHandler {
    static SustcManager sustcManagerInstance;

    /**
     * @url /sustc_manager/company_count
     * @Header username String
     * @Header password String
     * @Header role     String
     * @Method Get
     * @Response Integer
     * @ResponseExmaple 5
     */
    public static String getCompanyCount(Request request, Response response) {
        int companyNumber = getSustcManagerInstance().getCompanyCount(Util.getLogInfo(request));
        return String.valueOf(companyNumber);
    }

    /**
     * @url /sustc_manager/city_count
     * @Header username String
     * @Header password String
     * @Header role     String
     * @Method Get
     * @Response Integer
     * @ResponseExmaple 5
     */
    public static String getCityCount(Request request, Response response) {
        int cityCount = getSustcManagerInstance().getCityCount(Util.getLogInfo(request));
        return String.valueOf(cityCount);
    }

    /**
     * @url /sustc_manager/courier_count
     * @Header username String
     * @Header password String
     * @Header role     String
     * @Method Get
     * @Response Integer
     * @ResponseExmaple 5
     */
    public static String getCourierCount(Request request, Response response) {
        int courierCount = getSustcManagerInstance().getCourierCount(Util.getLogInfo(request));
        return String.valueOf(courierCount);
    }

    /**
     * @url /sustc_manager/ship_count
     * @Header username String
     * @Header password String
     * @Header role     String
     * @Method Get
     * @Response Integer
     * @ResponseExmaple 5
     */
    public static String getShipCount(Request request, Response response) {
        int shipCount = getSustcManagerInstance().getShipCount(Util.getLogInfo(request));
        return String.valueOf(shipCount);
    }

    /**
     * @url /sustc_manager/ship_info/:name
     * @Header username String
     * @Header password String
     * @Header role     String
     * @Method Get
     * @Response JSON
     * @ResponseExmaple {
     * name: "xxxx",
     * $class: "xxxx",
     * price: 12.34,
     * state: "xxxxxx",
     * retrieval: {
     * city: "xxxx",
     * courier: "xxxx",
     * },
     * delivery: {
     * city: "xxxx",
     * courier: "xxxx",
     * },
     * $import: {
     * city: "xxxx",
     * officer: "xxxx",
     * tax:  12.34
     * },
     * export: {
     * city: "xxxx",
     * officer: "xxxx",
     * tax:  12.34
     * }
     * }
     */
    public static String getItemInfo(Request request, Response response) {
        LogInfo logInfo = Util.getLogInfo(request);
        String itemCode = request.params(":code");

        ItemInfo itemInfo = getSustcManagerInstance().getItemInfo(logInfo, itemCode);
        return new Gson().toJson(itemInfo);
    }


    /**
     * @url /sustc_manager/ship_info/:name
     * @Header username String
     * @Header password String
     * @Header role     String
     * @Method Get
     * @Response JSON
     * @ResponseExmaple {
     * name: "xxxx",
     * owner: "xxxx",
     * sailing: true
     * }
     */
    public static String getShipInfo(Request request, Response response) {
        LogInfo logInfo = Util.getLogInfo(request);
        String shipName = request.params(":name");

        ShipInfo shipInfo = getSustcManagerInstance().getShipInfo(logInfo, shipName);
        return new Gson().toJson(shipInfo);
    }

    /**
     * @url /sustc_manager/container_info/:code
     * @Header username String
     * @Header password String
     * @Header role     String
     * @Method Get
     * @Response JSON
     * @ResponseExmaple {
     * type: "xxxx",
     * code: "xxxx",
     * using: true
     * }
     */
    public static String getContainerInfo(Request request, Response response) {
        LogInfo logInfo = Util.getLogInfo(request);
        String containerCode = request.params(":code");

        ContainerInfo containerInfo = getSustcManagerInstance().getContainerInfo(logInfo, containerCode);
        return new Gson().toJson(containerInfo);
    }

    /**
     * @url /sustc_manager/staff_info/:name
     * @Header username String
     * @Header password String
     * @Header role     String
     * @Method Get
     * @Response JSON
     * @ResponseExmaple {
     * LogInfo: {
     * username: "xxxxx",
     * password: "xxxxx",
     * },
     * company: "xxxx",
     * city: "xxxxxx",
     * isFemale: true,
     * age: 18,
     * phoneNumber: "13888888888"
     * }
     */
    public static String getStaffInfo(Request request, Response response) {
        LogInfo logInfo = Util.getLogInfo(request);
        String staffName = request.params(":name");

        StaffInfo staffInfo = getSustcManagerInstance().getStaffInfo(logInfo, staffName);
        return new Gson().toJson(staffInfo);
    }


    public static SustcManager getSustcManagerInstance() {
        if (sustcManagerInstance == null) {
            sustcManagerInstance = new SustcManager();
        }

        return sustcManagerInstance;
    }
}
