package SUSTech_CS307_Project2.Backend;

import SUSTech_CS307_Project2.Courier;
import SUSTech_CS307_Project2.Util;
import cs307.project2.interfaces.ItemInfo;
import cs307.project2.interfaces.ItemState;
import cs307.project2.interfaces.LogInfo;
import spark.Request;
import spark.Response;

public class CourierHandler {
    static Courier courierInstance;

    public static String newItem(Request request, Response response) {
        LogInfo logInfo = Util.getLogInfo(request);
        ItemInfo.RetrievalDeliveryInfo retrievalInfo = new ItemInfo.RetrievalDeliveryInfo(request.queryParams("from_city"), request.queryParams("retrieval_courier"));
        ItemInfo.RetrievalDeliveryInfo deliveryInfo = new ItemInfo.RetrievalDeliveryInfo(request.queryParams("to_city"), request.queryParams("delivery_courier"));
        ItemInfo.ImportExportInfo exportInfo = new ItemInfo.ImportExportInfo(request.queryParams("export_city"),
                request.queryParams("export_officer"),
                Double.parseDouble(request.queryParams("export_tax")));
        ItemInfo.ImportExportInfo importInfo = new ItemInfo.ImportExportInfo(request.queryParams("import_city"),
                request.queryParams("import_officer"),
                Double.parseDouble(request.queryParams("import_tax")));


        ItemInfo itemInfo = new ItemInfo(
                request.queryParams("name"),
                request.queryParams("class"),
                Double.parseDouble(request.queryParams("price")),
                Util.stateTextToObject(request.queryParams("state")),
                retrievalInfo,
                deliveryInfo,
                importInfo,
                exportInfo
        );

        return String.valueOf(getCourierInstance().newItem(logInfo, itemInfo));
    }

    public static String setItemState(Request request, Response response) {
        LogInfo logInfo = Util.getLogInfo(request);
        ItemState itemState = Util.stateTextToObject(request.queryParams("state"));
        String itemCode = request.params(":code");
        return String.valueOf(getCourierInstance().setItemState(logInfo, itemCode, itemState));
    }

    public static Courier getCourierInstance() {
        if (courierInstance == null) {
            courierInstance = new Courier();
        }
        return courierInstance;
    }
}
