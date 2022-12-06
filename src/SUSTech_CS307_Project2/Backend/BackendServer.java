package SUSTech_CS307_Project2.Backend;

import static spark.Spark.*;


public class BackendServer {
    public static void main(String[] args) {
        get("/sustc_manager/company_count", (SustcManagerHandler::getCompanyCount));
    }
}
