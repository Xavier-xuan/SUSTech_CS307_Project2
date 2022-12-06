package SUSTech_CS307_Project2.Backend;

import static spark.Spark.*;


public class BackendServer {
    public static void main(String[] args) {
        get("/sustc_manager/company_count", SustcManagerHandler::getCompanyCount);
        get("/sustc_manager/city_count", SustcManagerHandler::getCityCount);
        get("/sustc_manager/courier_count", SustcManagerHandler::getCourierCount);
        get("/sustc_manager/ship_count", SustcManagerHandler::getShipCount);
        get("/sustc_manager/item_info/:code", SustcManagerHandler::getItemInfo);
        get("/sustc_manager/ship_info/:name", SustcManagerHandler::getShipInfo);
        get("/sustc_manager/container_info/:code", SustcManagerHandler::getContainerInfo);
        get("/sustc_manager/staff_info/:name", SustcManagerHandler::getStaffInfo);


        post("/courier/new_item", CourierHandler::newItem);
        post("/courier/set_item_state/:code", CourierHandler::setItemState);

        after((request, response) -> {
            response.header("Content-Type", "application/json");
        });
    }
}
