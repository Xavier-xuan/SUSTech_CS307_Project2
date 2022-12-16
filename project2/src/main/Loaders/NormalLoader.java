package main.Loaders;

import main.ConnectionManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class NormalLoader {
    public static void loadFromFile(String RecordsCSV, int max) throws Exception {
        Connection con = ConnectionManager.getRootConnection();
        Statement operation = con.createStatement();
        con.setAutoCommit(false);
        operation.executeUpdate("alter table ship disable trigger all;");
        operation.executeUpdate("alter table city disable trigger all;");
        operation.executeUpdate("alter table company disable trigger all;");
        operation.executeUpdate("alter table container disable trigger all;");
        operation.executeUpdate("alter table item disable trigger all;");
        operation.executeUpdate("alter table port_city disable trigger all;");
        con.commit();



        int cnt;
        int MAXRECORD = max;
        File csv = new File(RecordsCSV);
        csv.setReadable(true);
        csv.setWritable(true);
        InputStreamReader isr = null;
        BufferedReader br = null;
        isr = new InputStreamReader(new FileInputStream(csv), StandardCharsets.UTF_8);
        br = new BufferedReader(isr);
        String line;
        cnt = 0;
        br.readLine();
        con.setAutoCommit(false);
        PreparedStatement cityR = con.prepareStatement("insert into city (name) values (?)  on conflict do nothing;");
        PreparedStatement cityD = con.prepareStatement("insert into city (name) values (?)  on conflict do nothing;");
        PreparedStatement exportCity = con.prepareStatement("insert into port_city (name) values (?)  on conflict do nothing;");
        PreparedStatement importCity = con.prepareStatement("insert into port_city (name) values (?)  on conflict do nothing;");
        PreparedStatement company = con.prepareStatement("insert into company (name) values (?)  on conflict do nothing;");
        PreparedStatement container = con.prepareStatement("insert into container (code, type) values (?,?)  on conflict do nothing;");
        PreparedStatement ship = con.prepareStatement("insert into ship (name, company_name) values (?,?)  on conflict do nothing;");
        PreparedStatement item = con.prepareStatement("insert into item (name, price, type, export_tax, " +
                "import_tax, export_city, import_city, export_officer, import_officer, from_city_name, " +
                "to_city_name, delivery_courier,retrieval_courier,container_code,ship_name,state) " +
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)  on conflict do nothing;");




        //tax cal after insertion
        String[] Info;

        while ((line = br.readLine()) != null && cnt <= MAXRECORD) {
            ++cnt;
            Info = line.split(",",-1);

            String ItemName = Info[0];
            String ItemClass = Info[1];
            String ItemPrice = Info[2];
            String RetrievalCity = Info[3];
            String RetrievalCourier = Info[4];
            String DeliveryCity = Info[5];
            String DeliveryCourier = Info[6];
            String ExportCity = Info[7];
            String ImportCity = Info[8];
            String ExportTax = Info[9];
            String ImportTax = Info[10];
            String ExportOfficer = Info[11];
            String ImportOfficer = Info[12];
            String ContainerCode = Info[13];
            String ContainerType = Info[14];
            String ShipName = Info[15];
            String CompanyName = Info[16];
            String ItemState = Info[17];

            if (ItemName.isEmpty()) continue;

            item.setString(1,ItemName);
            item.setDouble(2,Double.parseDouble(ItemPrice));
            item.setString(3,ItemClass);
            if (!ExportTax.isEmpty()) item.setDouble(4,Double.parseDouble(ExportTax));
            if (!ImportTax.isEmpty()) item.setDouble(5,Double.parseDouble(ImportTax));
            item.setString(6,ExportCity);
            item.setString(7,ImportCity);
            if (!ExportOfficer.isEmpty()) item.setString(8,ExportOfficer);
            if (!ImportOfficer.isEmpty()) item.setString(9,ImportOfficer);
            item.setString(10,RetrievalCity);
            item.setString(11,DeliveryCity);
            if (!DeliveryCourier.isEmpty()) item.setString(12,DeliveryCourier);
            if (!RetrievalCourier.isEmpty()) item.setString(13,RetrievalCourier);
            if (!ContainerCode.isEmpty()) item.setString(14,ContainerCode);
            if (!ShipName.isEmpty()) item.setString(15,ShipName);
            item.setString(16,ItemState);
            item.addBatch();
            cityR.setString(1, RetrievalCity);
            cityR.addBatch();
            cityD.setString(1, DeliveryCity);
            cityD.addBatch();
            exportCity.setString(1, ExportCity);
            exportCity.addBatch();
            importCity.setString(1, ImportCity);
            importCity.addBatch();
            company.setString(1,CompanyName);
            company.addBatch();
            container.setString(1,ContainerCode);
            container.setString(2,ContainerType);
            container.addBatch();
            ship.setString(1,ShipName);
            ship.setString(2,CompanyName);
            ship.addBatch();
        }
        cityR.executeBatch();
        cityD.executeBatch();
        exportCity.executeBatch();
        importCity.executeBatch();
        company.executeBatch();
        container.executeBatch();
        ship.executeBatch();






        operation.executeUpdate("alter table ship enable trigger all;");
        operation.executeUpdate("alter table city enable trigger all;");
        operation.executeUpdate("alter table company enable trigger all;");
        operation.executeUpdate("alter table container enable trigger all;");
        operation.executeUpdate("alter table item enable trigger all;");
        operation.executeUpdate("alter table port_city enable trigger all;");
        con.commit();
        con.setAutoCommit(true);
    }

//    public static void loadFromString()
}
