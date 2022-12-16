package main.Backend;

import main.*;
import main.interfaces.*;
import spark.Request;
import spark.Response;

public class CourierHandler {
    static Courier courierInstance;

    /**
     * @url /courier/new_item
     * @Header username String
     * @Header password String
     * @Header role     String
     * @Method Post
     * @RequestParam from_city          String
     * @RequestParam to_city            String
     * @RequestParam retrieval_courier  String
     * @RequestParam delivery_courier   String
     * @RequestParam export_city        String
     * @RequestParam export_officer     String
     * @RequestParam export_tax         float
     * @RequestParam import_city        String
     * @RequestParam import_officer     String
     * @RequestParam import_tax         float
     * @RequestParam name               String
     * @RequestParam class              String
     * @RequestParam price              float
     * @RequestParam state              String
     * @Response String
     * @ResponseExmaple true
     */
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

    /**
     * @url /courier/set_state
     * @Header username String
     * @Header password String
     * @Header role     String
     * @Method Post
     * @RequestParam state              String
     * @RequestParam code               String
     * @Response String
     * @ResponseExmaple true
     */
    public static String setItemState(Request request, Response response) {
        LogInfo logInfo = Util.getLogInfo(request);
        ItemState itemState = Util.stateTextToObject(request.queryParams("state"));
        String itemCode = request.queryParams("code");
        return String.valueOf(getCourierInstance().setItemState(logInfo, itemCode, itemState));
    }

    public static Courier getCourierInstance() {
        if (courierInstance == null) {
            courierInstance = new Courier();
        }
        return courierInstance;
    }
}
