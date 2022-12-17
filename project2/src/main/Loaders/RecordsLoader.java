package main.Loaders;

import main.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecordsLoader {
    private static int threads = 5;
    static int cntRecords;

    private static ExecutorService threadPool = Executors.newFixedThreadPool(threads);

    public static void loadFromFile(String RecordsCSV) throws Exception {
        long startTime = System.currentTimeMillis();
        Connection ops = ConnectionManager.getRootConnection();
        Statement operation = ops.createStatement();
        ops.setAutoCommit(false);
        operation.executeUpdate("alter table ship disable trigger all;");
        operation.executeUpdate("alter table city disable trigger all;");
        operation.executeUpdate("alter table company disable trigger all;");
        operation.executeUpdate("alter table container disable trigger all;");
        operation.executeUpdate("alter table item disable trigger all;");
        operation.executeUpdate("alter table port_city disable trigger all;");
        ops.commit();
        CountDownLatch latch = new CountDownLatch(threads);
        threadPool.execute(() ->{
            try {
                iCompany(RecordsCSV, ConnectionManager.getDMConnection());
            } catch (Exception e) {
                System.err.println(e);
            } finally {
                latch.countDown();
            }
        });
        threadPool.execute(() ->{
            try {
                iCity(RecordsCSV, ConnectionManager.getDMConnection());
            } catch (Exception e) {
                System.err.println(e);
            } finally {
                latch.countDown();
            }
        });
        threadPool.execute(() ->{
            try {
                iContainer(RecordsCSV, ConnectionManager.getDMConnection());
            } catch (Exception e) {
                System.err.println(e);
            } finally {
                latch.countDown();
            }
        });
        threadPool.execute(() ->{
            try {
                cntRecords=iItem(RecordsCSV, ConnectionManager.getDMConnection());
            } catch (Exception e) {
                System.err.println(e);
            } finally {
                latch.countDown();
            }
        });
        threadPool.execute(() ->{
            try {
                iShip(RecordsCSV, ConnectionManager.getDMConnection());
            } catch (Exception e) {
                System.err.println(e);
            } finally {
                latch.countDown();
            }
        });
        latch.await();
        operation.executeUpdate("alter table ship enable trigger all;");
        operation.executeUpdate("alter table city enable trigger all;");
        operation.executeUpdate("alter table company enable trigger all;");
        operation.executeUpdate("alter table container enable trigger all;");
        operation.executeUpdate("alter table item enable trigger all;");
        operation.executeUpdate("alter table port_city enable trigger all;");
        ops.commit();
        ops.setAutoCommit(true);
        long endTime = System.currentTimeMillis();
        threadPool.shutdown();
        System.out.printf("Loaded Records with MultiThread Loader: %d records, speed: %.2f records/s\n", cntRecords, (float)(cntRecords*1e3/(endTime-startTime)));
    }

    public static void loadFromString(String Records) throws Exception {
        long startTime = System.currentTimeMillis();
        Connection ops = ConnectionManager.getRootConnection();
        Statement operation = ops.createStatement();
        ops.setAutoCommit(false);
        operation.executeUpdate("alter table ship disable trigger all;");
        operation.executeUpdate("alter table city disable trigger all;");
        operation.executeUpdate("alter table company disable trigger all;");
        operation.executeUpdate("alter table container disable trigger all;");
        operation.executeUpdate("alter table item disable trigger all;");
        operation.executeUpdate("alter table port_city disable trigger all;");
        ops.commit();
        CountDownLatch latch = new CountDownLatch(threads);
        String [] RecordsCSV = Records.split("\n",-1);

        threadPool.execute(() ->{
            try {
                iCompany(RecordsCSV, ConnectionManager.getDMConnection());
            } catch (Exception e) {
                System.err.println(e);
            } finally {
                latch.countDown();
            }
        });
        threadPool.execute(() ->{
            try {
                iCity(RecordsCSV, ConnectionManager.getDMConnection());
            } catch (Exception e) {
                System.err.println(e);
            } finally {
                latch.countDown();
            }
        });
        threadPool.execute(() ->{
            try {
                iContainer(RecordsCSV, ConnectionManager.getDMConnection());
            } catch (Exception e) {
                System.err.println(e);
            } finally {
                latch.countDown();
            }
        });
        threadPool.execute(() ->{
            try {
                cntRecords=iItem(RecordsCSV, ConnectionManager.getDMConnection());
            } catch (Exception e) {
                System.err.println(e);
            } finally {
                latch.countDown();
            }
        });
        threadPool.execute(() ->{
            try {
                iShip(RecordsCSV, ConnectionManager.getDMConnection());
            } catch (Exception e) {
                System.err.println(e);
            } finally {
                latch.countDown();
            }
        });
        latch.await();
        operation.executeUpdate("alter table ship enable trigger all;");
        operation.executeUpdate("alter table city enable trigger all;");
        operation.executeUpdate("alter table company enable trigger all;");
        operation.executeUpdate("alter table container enable trigger all;");
        operation.executeUpdate("alter table item enable trigger all;");
        operation.executeUpdate("alter table port_city enable trigger all;");
        ops.commit();
        ops.setAutoCommit(true);
        long endTime = System.currentTimeMillis();
        threadPool.shutdown();
        System.out.printf("Loaded Records with MultiThread Loader: %d records, speed: %.2f records/s\n", cntRecords, (float)(cntRecords*1e3/(endTime-startTime)));
    }

    public static void iCompany (String filePath,Connection con) throws Exception {
        File csv = new File(filePath);
        csv.setReadable(true);
        csv.setWritable(true);
        InputStreamReader isr = new InputStreamReader(new FileInputStream(csv), StandardCharsets.UTF_8);
        BufferedReader br= new BufferedReader(isr);
        String line;
        br.readLine();
        con.setAutoCommit(false);
        PreparedStatement company = con.prepareStatement("insert into company (name) values (?)  on conflict do nothing;");
        //tax cal after insertion
        String[] Info;
        while ((line = br.readLine()) != null) {
            Info = line.split(",",-1);
            String CompanyName = Info[16];
            if (CompanyName.isEmpty()) continue;
            company.setString(1,CompanyName);
            company.addBatch();
        }
        company.executeBatch();
        company.clearBatch();
        con.commit();

//        long endTime=System.currentTimeMillis();
//        System.out.printf("Inserted: %d records, speed: %.2f records/s\n",max, (float)(max*1e3/(endTime-startTime)));
    }
    public static void iCity (String filePath, Connection con) throws Exception {
        File csv = new File(filePath);
        csv.setReadable(true);
        csv.setWritable(true);
        InputStreamReader isr = null;
        BufferedReader br = null;
        isr = new InputStreamReader(new FileInputStream(csv), StandardCharsets.UTF_8);
        br = new BufferedReader(isr);
        String line;
        br.readLine();
        con.setAutoCommit(false);
        PreparedStatement cityR = con.prepareStatement("insert into city (name) values (?)  on conflict do nothing;");
        PreparedStatement cityD = con.prepareStatement("insert into city (name) values (?)  on conflict do nothing;");
        PreparedStatement exportCity = con.prepareStatement("insert into port_city (name) values (?)  on conflict do nothing;");
        PreparedStatement importCity = con.prepareStatement("insert into port_city (name) values (?)  on conflict do nothing;");

        //tax cal after insertion
        String[] Info;

        while ((line = br.readLine()) != null) {
            Info = line.split(",",-1);
            String RetrievalCity = Info[3];
            String DeliveryCity = Info[5];
            String ExportCity = Info[7];
            String ImportCity = Info[8];

                cityR.setString(1, RetrievalCity);
                cityR.addBatch();
                cityD.setString(1, DeliveryCity);
                cityD.addBatch();
                exportCity.setString(1, ExportCity);
                exportCity.addBatch();
                importCity.setString(1, ImportCity);
                importCity.addBatch();
        }
        cityR.executeBatch();
        cityD.executeBatch();
        exportCity.executeBatch();
        importCity.executeBatch();
        cityR.clearBatch();
        cityD.clearBatch();
        exportCity.clearBatch();
        importCity.clearBatch();
        con.commit();
    }
    public static void iContainer (String filePath, Connection con) throws Exception {
        File csv = new File(filePath);
        csv.setReadable(true);
        csv.setWritable(true);
        InputStreamReader isr = new InputStreamReader(new FileInputStream(csv), StandardCharsets.UTF_8);
        BufferedReader br= new BufferedReader(isr);
        String line;
        br.readLine();
        con.setAutoCommit(false);
        PreparedStatement container = con.prepareStatement("insert into container (code, type) values (?,?)  on conflict do nothing;");
        //tax cal after insertion
        String[] Info;
        while ((line = br.readLine()) != null) {
            Info = line.split(",",-1);
            String ContainerCode = Info[13];
            String ContainerType = Info[14];
            if (ContainerCode.isEmpty()) continue;
            container.setString(1,ContainerCode);
            container.setString(2,ContainerType);
            container.addBatch();
        }
        container.executeBatch();
        container.clearBatch();
        con.commit();
    }
    public static void iShip (String filePath, Connection con) throws Exception {
        File csv = new File(filePath);
        csv.setReadable(true);
        csv.setWritable(true);
        InputStreamReader isr = new InputStreamReader(new FileInputStream(csv), StandardCharsets.UTF_8);
        BufferedReader br= new BufferedReader(isr);
        String line;
        br.readLine();
        con.setAutoCommit(false);
        PreparedStatement ship = con.prepareStatement("insert into ship (name, company_name) values (?,?)  on conflict do nothing;");
        //tax cal after insertion
        String[] Info;
        while ((line = br.readLine()) != null ) {
            Info = line.split(",",-1);
            String ShipName = Info[15];
            String CompanyName = Info[16];
            if (ShipName.isEmpty()) continue;
            ship.setString(1,ShipName);
            ship.setString(2,CompanyName);
            ship.addBatch();
        }
        ship.executeBatch();
        ship.clearBatch();
        con.commit();
    }
    public static int iItem (String filePath, Connection con) throws Exception {
        int cnt = 0;
        File csv = new File(filePath);
        csv.setReadable(true);
        csv.setWritable(true);
        InputStreamReader isr = new InputStreamReader(new FileInputStream(csv), StandardCharsets.UTF_8);
        BufferedReader br= new BufferedReader(isr);
        String line;
        br.readLine();
        con.setAutoCommit(false);
        PreparedStatement item = con.prepareStatement("insert into item (name, price, type, export_tax, " +
                "import_tax, export_city, import_city, export_officer, import_officer, from_city_name, " +
                "to_city_name, delivery_courier,retrieval_courier,container_code,ship_name,state) " +
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)  on conflict do nothing;");
        //tax cal after insertion
        String[] Info;
        while ((line = br.readLine()) != null) {
            cnt++;
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
            String ShipName = Info[15];
            String ItemState = Info[17];
            if (ItemName.isEmpty()) continue;
            item.setString(1,ItemName);
            item.setDouble(2,Double.parseDouble(ItemPrice));
            item.setString(3,ItemClass);
            item.setDouble(4,Double.parseDouble(ExportTax));
            item.setDouble(5,Double.parseDouble(ImportTax));
            item.setString(6,ExportCity);
            item.setString(7,ImportCity);
            item.setString(8,ExportOfficer);
            item.setString(9,ImportOfficer);
            item.setString(10,RetrievalCity);
            item.setString(11,DeliveryCity);
            item.setString(12,DeliveryCourier);
            item.setString(13,RetrievalCourier);
            item.setString(14,ContainerCode);
            item.setString(15,ShipName);
            item.setString(16,ItemState);
            item.addBatch();
        }
        item.executeBatch();
        item.clearBatch();
        con.commit();
        return cnt;
    }

    public static void iCompany(String [] data, Connection con) throws Exception {
        String line;
        String [] Info;
        con.setAutoCommit(false);
        PreparedStatement company = con.prepareStatement("insert into company (name) values (?)  on conflict do nothing;");
        //tax cal after insertion
        for (int i = 1; i < data.length; i++) {
            line = data[i];
            Info = line.split(",",-1);
            if (Info.length!=18) continue;
            String CompanyName = Info[16];
            if (CompanyName.isEmpty()) continue;
            company.setString(1,CompanyName);
            company.addBatch();
        }
        company.executeBatch();
        company.clearBatch();
        con.commit();
    }
    public static void iCity (String [] data, Connection con) throws Exception {
        String line;
        String[] Info;
        con.setAutoCommit(false);
        PreparedStatement cityR = con.prepareStatement("insert into city (name) values (?)  on conflict do nothing;");
        PreparedStatement cityD = con.prepareStatement("insert into city (name) values (?)  on conflict do nothing;");
        PreparedStatement exportCity = con.prepareStatement("insert into port_city (name) values (?)  on conflict do nothing;");
        PreparedStatement importCity = con.prepareStatement("insert into port_city (name) values (?)  on conflict do nothing;");
        for (int i = 1; i < data.length; i++) {
            line = data[i];
            Info = line.split(",",-1);
            if (Info.length!=18) continue;
            String RetrievalCity = Info[3];
            String DeliveryCity = Info[5];
            String ExportCity = Info[7];
            String ImportCity = Info[8];

            cityR.setString(1, RetrievalCity);
            cityR.addBatch();
            cityD.setString(1, DeliveryCity);
            cityD.addBatch();
            exportCity.setString(1, ExportCity);
            exportCity.addBatch();
            importCity.setString(1, ImportCity);
            importCity.addBatch();
        }
        cityR.executeBatch();
        cityD.executeBatch();
        exportCity.executeBatch();
        importCity.executeBatch();
        cityR.clearBatch();
        cityD.clearBatch();
        exportCity.clearBatch();
        importCity.clearBatch();
        con.commit();
    }
    public static void iContainer (String [] data, Connection con) throws Exception {
        String line;
        String[] Info;
        con.setAutoCommit(false);
        PreparedStatement container = con.prepareStatement("insert into container (code, type) values (?,?)  on conflict do nothing;");
        //tax cal after insertion
        for (int i = 1; i < data.length; i++) {
            line = data[i];
            Info = line.split(",",-1);
            if (Info.length!=18) continue;
            String ContainerCode = Info[13];
            String ContainerType = Info[14];
            if (ContainerCode.isEmpty()) continue;
            container.setString(1,ContainerCode);
            container.setString(2,ContainerType);
            container.addBatch();
        }
        container.executeBatch();
        container.clearBatch();
        con.commit();
    }
    public static void iShip (String [] data, Connection con) throws Exception {
        String line;
        String[] Info;
        con.setAutoCommit(false);
        PreparedStatement ship = con.prepareStatement("insert into ship (name, company_name) values (?,?)  on conflict do nothing;");
        for (int i = 1; i < data.length; i++) {
            line = data[i];
            Info = line.split(",",-1);
            if (Info.length!=18) continue;
            String ShipName = Info[15];
            String CompanyName = Info[16];
            if (ShipName.isEmpty()) continue;
            ship.setString(1,ShipName);
            ship.setString(2,CompanyName);
            ship.addBatch();
        }
        ship.executeBatch();
        ship.clearBatch();
        con.commit();
    }
    public static int iItem (String [] data, Connection con) throws Exception {
        String line;
        String[] Info;
        int cnt = 0;
        con.setAutoCommit(false);
        PreparedStatement item = con.prepareStatement("insert into item (name, price, type, export_tax, " +
                "import_tax, export_city, import_city, export_officer, import_officer, from_city_name, " +
                "to_city_name, delivery_courier,retrieval_courier,container_code,ship_name,state) " +
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)  on conflict do nothing;");
        //tax cal after insertion
        for (int i = 1; i < data.length; i++) {
            line = data[i];
            Info = line.split(",",-1);
            if (Info.length!=18) continue;
            cnt++;
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
            String ShipName = Info[15];
            String ItemState = Info[17];
            if (ItemName.isEmpty()) continue;
            item.setString(1,ItemName);
            item.setDouble(2,Double.parseDouble(ItemPrice));
            item.setString(3,ItemClass);
            item.setDouble(4,Double.parseDouble(ExportTax));
            item.setDouble(5,Double.parseDouble(ImportTax));
            item.setString(6,ExportCity);
            item.setString(7,ImportCity);
            item.setString(8,ExportOfficer);
            item.setString(9,ImportOfficer);
            item.setString(10,RetrievalCity);
            item.setString(11,DeliveryCity);
            item.setString(12,DeliveryCourier);
            item.setString(13,RetrievalCourier);
            item.setString(14,ContainerCode);
            item.setString(15,ShipName);
            item.setString(16,ItemState);
            item.addBatch();
        }
        item.executeBatch();
        item.clearBatch();
        con.commit();
        return cnt;
    }

    /*
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
     */
}
