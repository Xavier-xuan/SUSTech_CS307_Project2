package main.Backend;

import main.*;
import main.interfaces.LogInfo;
import org.eclipse.jetty.util.log.Log;
import spark.Request;
import spark.Response;

import java.sql.Statement;

public class AuthHandler {



    /**
     * @url /login
     * @Method Post
     * @RequestParam username String
     * @RequestParam password String
     * @RequestParam role     courier | company manager | seaport officer | sustc manager
     * @Response String
     * @ResponseExmaple true|false
     */
    public static String login(Request request, Response response) {
        String username = request.queryParams("username");
        String password = request.queryParams("password");
        String roleText = request.queryParams("role").toLowerCase().trim();
        LogInfo logInfo;
        LogInfo.StaffType role;
        switch (roleText) {
            case "courier":
                role = LogInfo.StaffType.Courier;
                logInfo = new LogInfo(username, role, password);
                return String.valueOf(new Courier().login(logInfo));
            case "company manager":
                role = LogInfo.StaffType.CompanyManager;
                logInfo = new LogInfo(username, role, password);
                return String.valueOf(new CompanyManager().login(logInfo));
            case "seaport officer":
                role = LogInfo.StaffType.SeaportOfficer;
                logInfo = new LogInfo(username, role, password);
                return String.valueOf(new SeaportOfficer().login(logInfo));
            case "sustc manager":
                role = LogInfo.StaffType.SustcManager;
                logInfo = new LogInfo(username, role, password);
                return String.valueOf(new SustcManager().login(logInfo));
            default:
                return String.valueOf(false);
        }
    }
}
