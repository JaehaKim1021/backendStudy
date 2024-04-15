package db;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.*;

public class WifiFunction2 {

    public String getDataTest(int start, int end) throws IOException{
        StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088"); /*URL*/
        urlBuilder.append("/" + URLEncoder.encode("664648704d6a656139334c626c6c53", "UTF-8")); /*인증키 (sample사용시에는 호출시 제한됩니다.)*/
        urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8")); /*요청파일타입 (xml,xmlf,xls,json) */
        urlBuilder.append("/" + URLEncoder.encode("TbPublicWifiInfo", "UTF-8")); /*서비스명 (대소문자 구분 필수입니다.)*/
        urlBuilder.append("/" + URLEncoder.encode(String.valueOf(start), "UTF-8")); /*요청시작위치 (sample인증키 사용시 5이내 숫자)*/
        urlBuilder.append("/" + URLEncoder.encode(String.valueOf(end), "UTF-8")); /*요청종료위치(sample인증키 사용시 5이상 숫자 선택 안 됨)*/


        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode()); /* 연결 자체에 대한 확인이 필요하므로 추가합니다.*/
        BufferedReader rd;

        // 서비스코드가 정상이면 200~300사이의 숫자가 나옵니다.
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        
        return sb.toString();
    }
    
    public int TotalCnt() throws IOException {
        String OneData = getDataTest(1,1);
        Gson gson = new Gson();

        JsonObject wifiDataDto = gson.fromJson(OneData, JsonObject.class);
        JsonObject totalcnt = gson.fromJson(wifiDataDto.get("TbPublicWifiInfo"),JsonObject.class);


        return Integer.parseInt(totalcnt.get("list_total_count").toString());
    }

    public void SaveToDB() throws IOException {

        String url = "jdbc:mariadb://192.168.109.128:3306/wifidb";
        String dbUserId = "testuser1";
        String dbPassword = "991021";

        Gson gson = new Gson();


        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        int start = 0;
        int end = 0;
        int total = 0;

        int TOTALCNT = TotalCnt();
        int pageEnd = TOTALCNT / 1000;
        int pageEndRemain = TOTALCNT % 1000;

        try {
            connection = DriverManager.getConnection(url, dbUserId, dbPassword);

            preparedStatement = null;

            for (int k = 0; k <= pageEnd; k++) {
                start = (int) Math.pow(10, 3) * k + 1;

                if (k == pageEnd) {
                    end = start + pageEndRemain - 1;
                } else {
                    end = (int) Math.pow(10, 3) * (k + 1);
                }

                String jsonData = getDataTest(start, end);


                JsonObject wifiDataDto = gson.fromJson(jsonData, JsonObject.class);
                JsonObject totalcnt = gson.fromJson(wifiDataDto.get("TbPublicWifiInfo"), JsonObject.class);
                JsonArray row = gson.fromJson(totalcnt.get("row"), JsonArray.class);


                Wifi2 wifi;
                for (int i = 0; i < row.size(); i++) {
                    wifi = new Wifi2();
                    System.out.println(row.get(i));
                    JsonObject eachRow = gson.fromJson(row.get(i), JsonObject.class);
                    System.out.println(eachRow.get("X_SWIFI_WRDOFC").toString());


                    String sql = "insert WIFI_INFO(X_SWIFI_MGR_NO, X_SWIFI_WRDOFC, X_SWIFI_MAIN_NM, X_SWIFI_ADRES1, X_SWIFI_ADRES2, " +
                            "                 X_SWIFI_INSTL_FLOOR, X_SWIFI_INSTL_TY, X_SWIFI_INSTL_MBY, X_SWIFI_SVC_SE, X_SWIFI_CMCWR, " +
                            "                 X_SWIFI_CNSTC_YEAR, X_SWIFI_INOUT_DOOR, X_SWIFI_REMARS3, LAT, LNT, WORK_DTTM)" +
                            " value (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";


                    wifi.setMGR_NO(eachRow.get("X_SWIFI_MGR_NO").toString().replaceAll("\\\"",""));
                    wifi.setWRDOFC(eachRow.get("X_SWIFI_WRDOFC").toString().replaceAll("\\\"",""));
                    wifi.setMAIN_NM(eachRow.get("X_SWIFI_MAIN_NM").toString().replaceAll("\\\"",""));
                    wifi.setADRES1(eachRow.get("X_SWIFI_ADRES1").toString().replaceAll("\\\"",""));
                    wifi.setADRES2(eachRow.get("X_SWIFI_ADRES2").toString().replaceAll("\\\"",""));
                    wifi.setINSTL_FLOOR(eachRow.get("X_SWIFI_INSTL_FLOOR").toString().replaceAll("\\\"",""));
                    wifi.setINSTL_TY(eachRow.get("X_SWIFI_INSTL_TY").toString().replaceAll("\\\"",""));
                    wifi.setINSTL_MBY(eachRow.get("X_SWIFI_INSTL_MBY").toString().replaceAll("\\\"",""));
                    wifi.setSVC_SE(eachRow.get("X_SWIFI_SVC_SE").toString().replaceAll("\\\"",""));
                    wifi.setCMCWR(eachRow.get("X_SWIFI_CMCWR").toString().replaceAll("\\\"",""));
                    wifi.setCNSTC_YEAR(eachRow.get("X_SWIFI_CNSTC_YEAR").toString().replaceAll("\\\"",""));
                    wifi.setINOUT_DOOR(eachRow.get("X_SWIFI_INOUT_DOOR").toString().replaceAll("\\\"",""));
                    wifi.setREMARS3(eachRow.get("X_SWIFI_REMARS3").toString().replaceAll("\\\"",""));
                    wifi.setLAT(Double.parseDouble(eachRow.get("LAT").toString().replaceAll("\\\"","")));
                    wifi.setLNT(Double.parseDouble(eachRow.get("LNT").toString().replaceAll("\\\"","")));
                    wifi.setWORK_DTTM(eachRow.get("WORK_DTTM").toString().replaceAll("\\\"",""));


                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, wifi.getMGR_NO());
                    preparedStatement.setString(2, wifi.getWRDOFC());
                    preparedStatement.setString(3, wifi.getMAIN_NM());
                    preparedStatement.setString(4, wifi.getADRES1());
                    preparedStatement.setString(5, wifi.getADRES2());
                    preparedStatement.setString(6, wifi.getINSTL_FLOOR());
                    preparedStatement.setString(7, wifi.getINSTL_TY());
                    preparedStatement.setString(8, wifi.getINSTL_MBY());
                    preparedStatement.setString(9, wifi.getSVC_SE());
                    preparedStatement.setString(10, wifi.getCMCWR());
                    preparedStatement.setString(11, wifi.getCNSTC_YEAR());
                    preparedStatement.setString(12, wifi.getINOUT_DOOR());
                    preparedStatement.setString(13, wifi.getREMARS3());
                    preparedStatement.setDouble(14, wifi.getLAT());
                    preparedStatement.setDouble(15, wifi.getLNT());
                    preparedStatement.setString(16, wifi.getWORK_DTTM());

                    int affected = preparedStatement.executeUpdate();

                    if (affected > 0) {
                        System.out.println("저장 성공");
                    } else {
                        System.out.println("저장 실패");
                    }

                    total++;
                    System.out.println(total);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {

            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if (preparedStatement != null && !preparedStatement.isClosed()) {
                    preparedStatement.close();

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

    }

}
