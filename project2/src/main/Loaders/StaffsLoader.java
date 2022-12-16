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

public class StaffsLoader {
    private static int threads = 4;
    private static int staffCnt;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(threads);

    public static void loadFromFile(String RecordsCSV) throws Exception {
        long startTime = System.currentTimeMillis();
        Connection ops = ConnectionManager.getRootConnection();
        Statement operation = ops.createStatement();
        ops.setAutoCommit(false);
        operation.executeUpdate("alter table company_manager disable trigger all;");
        operation.executeUpdate("alter table courier disable trigger all;");
        operation.executeUpdate("alter table officer disable trigger all;");
        operation.executeUpdate("alter table sustc_manager disable trigger all;");
        ops.commit();
        CountDownLatch latch = new CountDownLatch(threads);
        threadPool.execute(() ->{
            try {
                iCompanyManager(RecordsCSV, ConnectionManager.getDMConnection());
            } catch (Exception e) {
                System.err.println(e);
            } finally {
                latch.countDown();
            }
        });
        threadPool.execute(() ->{
            try {
                iOfficer(RecordsCSV, ConnectionManager.getDMConnection());
            } catch (Exception e) {
                System.err.println(e);
            } finally {
                latch.countDown();
            }
        });
        threadPool.execute(() ->{
            try {
                iCourier(RecordsCSV, ConnectionManager.getDMConnection());
            } catch (Exception e) {
                System.err.println(e);
            } finally {
                latch.countDown();
            }
        });
        threadPool.execute(() ->{
            try {
                staffCnt = iSustcManager(RecordsCSV, ConnectionManager.getDMConnection());
            } catch (Exception e) {
                System.err.println(e);
            } finally {
                latch.countDown();
            }
        });

        latch.await();
        operation.executeUpdate("alter table company_manager enable trigger all;");
        operation.executeUpdate("alter table courier enable trigger all;");
        operation.executeUpdate("alter table officer enable trigger all;");
        operation.executeUpdate("alter table sustc_manager enable trigger all;");
        ops.commit();
        ops.setAutoCommit(true);
        long endTime = System.currentTimeMillis();
        threadPool.shutdown();
        System.out.printf("Loaded Staffs with MultiThread Loader: %d records, speed: %.2f records/s\n",staffCnt, (float)(staffCnt*1e3/(endTime-startTime)));
    }
    public static void iCompanyManager (String filePath, Connection con) throws Exception {
        File csv = new File(filePath);
        csv.setReadable(true);
        csv.setWritable(true);
        InputStreamReader isr = new InputStreamReader(new FileInputStream(csv), StandardCharsets.UTF_8);
        BufferedReader br= new BufferedReader(isr);
        String line;
        br.readLine();
        con.setAutoCommit(false);
        PreparedStatement CM = con.prepareStatement("insert into company_manager (name,phone_number,gender,age,password,company_name) values (?,?,?,?,?,?)  on conflict do nothing;");
        //tax cal after insertion
        String[] Info;
        while ((line = br.readLine()) != null ) {
            Info = line.split(",",-1);
            String Name = Info[0];
            String Type = Info[1];
            String Company = Info[2];
            String Gender = Info[4];
            String Age = Info[5];
            String Phone = Info[6];
            String Passwd = Info[7];
            if (!Type.equals("Company Manager")) continue;
            CM.setString(1,Name);
            CM.setString(2,Phone);
            CM.setString(3,Gender);
            CM.setInt(4,Integer.parseInt(Age));
            CM.setString(5,Passwd);
            CM.setString(6,Company);
            CM.addBatch();
        }
        CM.executeBatch();
        CM.clearBatch();
        con.commit();
    }
    public static void iCourier (String filePath,Connection con) throws Exception {
        File csv = new File(filePath);
        csv.setReadable(true);
        csv.setWritable(true);
        InputStreamReader isr = new InputStreamReader(new FileInputStream(csv), StandardCharsets.UTF_8);
        BufferedReader br= new BufferedReader(isr);
        String line;
        br.readLine();
        con.setAutoCommit(false);
        PreparedStatement Courier = con.prepareStatement("insert into courier (name,phone_number,gender,age,password,company_name,city_name) values (?,?,?,?,?,?,?)  on conflict do nothing;");
        //tax cal after insertion
        String[] Info;
        while ((line = br.readLine()) != null ) {
            Info = line.split(",",-1);
            String Name = Info[0];
            String Type = Info[1];
            String Company = Info[2];
            String City = Info[3];
            String Gender = Info[4];
            String Age = Info[5];
            String Phone = Info[6];
            String Passwd = Info[7];
            if (!Type.equals("Courier")) continue;
            Courier.setString(1,Name);
            Courier.setString(2,Phone);
            Courier.setString(3,Gender);
            Courier.setInt(4,Integer.parseInt(Age));
            Courier.setString(5,Passwd);
            Courier.setString(6,Company);
            Courier.setString(7,City);
            Courier.addBatch();
        }
        Courier.executeBatch();
        Courier.clearBatch();
        con.commit();
    }
    public static void iOfficer (String filePath,Connection con) throws Exception {

        File csv = new File(filePath);
        csv.setReadable(true);
        csv.setWritable(true);
        InputStreamReader isr = new InputStreamReader(new FileInputStream(csv), StandardCharsets.UTF_8);
        BufferedReader br= new BufferedReader(isr);
        String line;

        br.readLine();
        con.setAutoCommit(false);
        PreparedStatement Officer = con.prepareStatement("insert into officer (name,phone_number,gender,age,password,port_city_name) values (?,?,?,?,?,?)  on conflict do nothing;");
        //tax cal after insertion
        String[] Info;
        while ((line = br.readLine()) != null) {
            Info = line.split(",",-1);
            String Name = Info[0];
            String Type = Info[1];
            String City = Info[3];
            String Gender = Info[4];
            String Age = Info[5];
            String Phone = Info[6];
            String Passwd = Info[7];
            if (!Type.equals("Seaport Officer")) continue;
            Officer.setString(1,Name);
            Officer.setString(2,Phone);
            Officer.setString(3,Gender);
            Officer.setInt(4,Integer.parseInt(Age));
            Officer.setString(5,Passwd);
            Officer.setString(6,City);
            Officer.addBatch();
        }
        Officer.executeBatch();
        Officer.clearBatch();
        con.commit();
    }
    public static int iSustcManager (String filePath, Connection con) throws Exception {
        int cnt;

        File csv = new File(filePath);
        csv.setReadable(true);
        csv.setWritable(true);
        InputStreamReader isr = new InputStreamReader(new FileInputStream(csv), StandardCharsets.UTF_8);
        BufferedReader br= new BufferedReader(isr);
        String line;
        cnt = 0;
        br.readLine();
        con.setAutoCommit(false);
        PreparedStatement SustcManager = con.prepareStatement("insert into sustc_manager (name,phone_number,gender,age,password) values (?,?,?,?,?)  on conflict do nothing;");
        //tax cal after insertion
        String[] Info;
        while ((line = br.readLine()) != null) {
            ++cnt;
            Info = line.split(",",-1);
            String Name = Info[0];
            String Type = Info[1];
            String Gender = Info[4];
            String Age = Info[5];
            String Phone = Info[6];
            String Passwd = Info[7];
            if (!Type.equals("SUSTC Department Manager")) continue;
            SustcManager.setString(1,Name);
            SustcManager.setString(2,Phone);
            SustcManager.setString(3,Gender);
            SustcManager.setInt(4,Integer.parseInt(Age));
            SustcManager.setString(5,Passwd);
            SustcManager.addBatch();
        }
        SustcManager.executeBatch();
        SustcManager.clearBatch();
        con.commit();
        return cnt;
    }


}
