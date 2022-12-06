package SUSTech_CS307_Project2.Backend;

import SUSTech_CS307_Project2.SeaportOfficer;
import SUSTech_CS307_Project2.Util;
import com.google.gson.Gson;
import cs307.project2.interfaces.LogInfo;
import spark.Request;
import spark.Response;

public class SeaportOfficerHandler {
    static SeaportOfficer seaportOfficer;


    public static String getAllItemsAtPort(Request request, Response response) {
        LogInfo logInfo = Util.getLogInfo(request);

        return new Gson().toJson(getSeaportOfficer().getAllItemsAtPort(logInfo));
    }

    public static String setItemCheckState(Request request, Response response) {
        LogInfo logInfo = Util.getLogInfo(request);
        String itemName = request.queryParams("item_name");
        boolean success = Boolean.parseBoolean(request.queryParams("success"));

        return String.valueOf(getSeaportOfficer().setItemCheckState(logInfo, itemName, success));
    }

    public static SeaportOfficer getSeaportOfficer() {
        if (seaportOfficer == null) {
            seaportOfficer = new SeaportOfficer();
        }

        return seaportOfficer;
    }
}
