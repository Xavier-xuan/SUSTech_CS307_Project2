package SUSTech_CS307_Project2.Backend;

import SUSTech_CS307_Project2.SustcManager;
import SUSTech_CS307_Project2.Util;
import com.google.gson.Gson;
import cs307.project2.interfaces.*;
import spark.Request;
import spark.Response;

public class SustcManagerHandler {
    static SustcManager sustcManagerInstance;


    public static String getCompanyCount(Request request, Response response) {
        int companyNumber = getSustcManagerInstance().getCompanyCount(Util.getLogInfo(request));
        return String.valueOf(companyNumber);
    }

    public static String getCityCount(Request request, Response response) {
        int cityCount = getSustcManagerInstance().getCityCount(Util.getLogInfo(request));
        return String.valueOf(cityCount);
    }

    public static String getCourierCount(Request request, Response response) {
        int courierCount = getSustcManagerInstance().getCourierCount(Util.getLogInfo(request));
        return String.valueOf(courierCount);
    }

    public static String getShipCount(Request request, Response response) {
        int shipCount = getSustcManagerInstance().getShipCount(Util.getLogInfo(request));
        return String.valueOf(shipCount);
    }

    public static String getItemInfo(Request request, Response response) {
        LogInfo logInfo = Util.getLogInfo(request);
        String itemCode = request.params(":code");

        ItemInfo itemInfo = getSustcManagerInstance().getItemInfo(logInfo, itemCode);
        return new Gson().toJson(itemInfo);
    }

    public static String getShipInfo(Request request, Response response) {
        LogInfo logInfo = Util.getLogInfo(request);
        String shipName = request.params(":name");

        ShipInfo shipInfo = getSustcManagerInstance().getShipInfo(logInfo, shipName);
        return new Gson().toJson(shipInfo);
    }

    public static String getContainerInfo(Request request, Response response) {
        LogInfo logInfo = Util.getLogInfo(request);
        String containerCode = request.params(":code");

        ContainerInfo containerInfo = getSustcManagerInstance().getContainerInfo(logInfo, containerCode);
        return new Gson().toJson(containerInfo);
    }

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
