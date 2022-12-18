package main.Backend;

import main.DBManipulation;

import static spark.Spark.*;


public class BackendServer {
    public static void main(String[] args) {
        DBManipulation dm = new DBManipulation("127.0.0.1/project2","postgres","123456");
        dm.$importFromFile(dm.tablePath, dm.staffPath);
        port(22307);
        post("/login", AuthHandler::login);
        get("/sustc_manager/company_count", SustcManagerHandler::getCompanyCount);
        get("/sustc_manager/city_count", SustcManagerHandler::getCityCount);
        get("/sustc_manager/courier_count", SustcManagerHandler::getCourierCount);
        get("/sustc_manager/ship_count", SustcManagerHandler::getShipCount);
        get("/sustc_manager/item_info/:name", SustcManagerHandler::getItemInfo);
        get("/sustc_manager/ship_info/:name", SustcManagerHandler::getShipInfo);
        get("/sustc_manager/container_info/:code", SustcManagerHandler::getContainerInfo);
        get("/sustc_manager/staff_info/:name", SustcManagerHandler::getStaffInfo);


        post("/courier/new_item", CourierHandler::newItem);
        post("/courier/set_item_state", CourierHandler::setItemState);

        get("/officer/get_all_items_at_port", SeaportOfficerHandler::getAllItemsAtPort);
        post("/officer/set_item_check_state", SeaportOfficerHandler::setItemCheckState);

        get("/company_manager/get_import_tax_rate/:city/:item_class", CompanyManagerHandler::getImportTaxRate);
        get("/company_manager/get_export_tax_rate/:city/:item_class", CompanyManagerHandler::getExportTaxRate);
        post("/company_manager/load_item_to_container", CompanyManagerHandler::loadItemToContainer);
        post("/company_manager/load_container_to_ship", CompanyManagerHandler::loadContainerToShip);
        post("/company_manager/ship_start_sailing", CompanyManagerHandler::shipStartSailing);
        post("/company_manager/unload_item", CompanyManagerHandler::unloadItem);
        post("/company_manager/item_wait_for_checking", CompanyManagerHandler::itemWaitForChecking);

        after((request, response) -> {
            response.header("Content-Type", "application/json");
        });
    }
}
