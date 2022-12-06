package SUSTech_CS307_Project2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    public static String host = "127.0.0.1";
    public static String port = "5432";
    public static String database = "postgres";
    public static String schema = "project2";
    public static String baseUrl = "jdbc:postgresql://" + host + ":" + port;

    public static void updateBaseUrl(){
        baseUrl = "jdbc:postgresql://" + host + ":" + port + "/" + database + "?currentSchema=" + schema;
    }

    /*
    Other Settings
     */
    public static boolean encryptPassword = true;

    /*
    Root User Credentials
     */
    public static String rootUsername = "postgres";
    public static String rootPassword = "postgres";
    /*
    Courier Credentials
     */
    public static String courierUsername = "courier";
    public static String courierPassword = "courier_password";

    /*
    Officer Credentials
     */
    public static String officerUsername = "officer";
    public static String officerPassword = "officer_password";

    /*
    Company Manager Credentials
     */
    public static String companyManagerUsername = "company_manager";
    public static String companyManagerPassword = "company_manager_password";

    /*
    Department Manager Credentials
     */
    public static String sustcManagerUsername = "sustc_manager";
    public static String sustcManagerPassword = "sustc_manager_password";

    /*
    Temp
     */
    static Connection rootConnection, courierConnection, officerConnection, companyManagerConnection, sustcManagerConnection;

    /*
    Getters
     */

    public static Connection getRootConnection() {
        /*
        Initialize
         */
        if (rootConnection == null) {
            try {
                rootConnection = DriverManager.getConnection(baseUrl, rootUsername, rootPassword);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return rootConnection;
    }

    public static Connection getCourierConnection() {
        /*
        Initialize
         */
        if (courierConnection == null) {
            try {
                courierConnection = DriverManager.getConnection(baseUrl, courierUsername, courierPassword);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return courierConnection;
    }

    public static Connection getOfficerConnection() {
        /*
        Initialize
         */
        if (officerConnection == null) {
            try {
                officerConnection = DriverManager.getConnection(baseUrl, officerUsername, officerPassword);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return officerConnection;
    }

    public static Connection getCompanyManagerConnection() {
        /*
        Initialize
         */
        if (companyManagerConnection == null) {
            try {
                companyManagerConnection = DriverManager.getConnection(baseUrl, companyManagerUsername, companyManagerPassword);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return companyManagerConnection;
    }

    public static Connection getSustcManagerConnection() {
         /*
        Initialize
         */
        if (sustcManagerConnection == null) {
            try {
                sustcManagerConnection = DriverManager.getConnection(baseUrl, sustcManagerUsername, sustcManagerPassword);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return sustcManagerConnection;
    }
}
