package db;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class LocationService {
	
    public List<LocationInfo2> list() {
    	
    	List<LocationInfo2> locationList = new ArrayList<>();
    	
        String url = "jdbc:mariadb://192.168.109.128:3306/wifidb";
        String dbUserId = "testuser1";
        String dbPassword = "991021";

        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Connection connection = null;
        Statement statement = null;
        PreparedStatement preparedStatement = null;
        CallableStatement callableStatement = null;
        ResultSet rs = null;

        String memberTypeValue = "email";


        try {
            connection = DriverManager.getConnection(url, dbUserId, dbPassword);

            statement = null;
            preparedStatement = null;
            callableStatement = null;


            String sql = "select ID, LAT, LNT, SEARCH_DATE"
            		+ " from HISTORY_INFO"
            		+" ORDER BY ID DESC;";

            preparedStatement = connection.prepareStatement(sql);

            rs = preparedStatement.executeQuery();


            while (rs.next()) {
            	int idValue = rs.getInt("ID");
            	double latValue = rs.getDouble("LAT");
            	double lntValue = rs.getDouble("LNT");
////            	Date date = new Date();
//            	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            	
            	String dateTimeValue = rs.getString("SEARCH_DATE");
                
                LocationInfo2 locationinfo = new LocationInfo2();
                locationinfo.setID(idValue);
                locationinfo.setLat(latValue);
                locationinfo.setLnt(lntValue);
                locationinfo.setDateTime(dateTimeValue);
                
                locationList.add(locationinfo);


                //System.out.println(memberType + " " + userId + " " + password + " " + name);
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
        
        return locationList;
    }

    
    public List<Wifi2> listW(double lat, double lnt) {
    	
    	List<Wifi2> wifiList = new ArrayList<>();
    	
        String url = "jdbc:mariadb://192.168.109.128:3306/wifidb";
        String dbUserId = "testuser1";
        String dbPassword = "991021";


        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Connection connection = null;
        Statement statement = null;
        PreparedStatement preparedStatement = null;
        CallableStatement callableStatement = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(url, dbUserId, dbPassword);

            statement = null;
            preparedStatement = null;
            callableStatement = null;


            String sql = "select X_SWIFI_MGR_NO, X_SWIFI_WRDOFC, X_SWIFI_MAIN_NM, X_SWIFI_ADRES1, X_SWIFI_ADRES2,"
            		+ "                 X_SWIFI_INSTL_FLOOR, X_SWIFI_INSTL_TY, X_SWIFI_INSTL_MBY, X_SWIFI_SVC_SE, X_SWIFI_CMCWR,"
            		+ "                 X_SWIFI_CNSTC_YEAR, X_SWIFI_INOUT_DOOR, X_SWIFI_REMARS3, LAT, LNT, WORK_DTTM,"
            		+ " st_distance_sphere(point(?, ?),point(LNT,LAT)) AS DISTANCE\r\n"
            		+ " from WIFI_INFO"
            		+ " order by DISTANCE"
            		+ " limit 20;";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1,lnt);
            preparedStatement.setDouble(2,lat);

            rs = preparedStatement.executeQuery();


            while (rs.next()) {
            	String MGR_NOValue = rs.getString("X_SWIFI_MGR_NO");
            	String WRDOFCValue = rs.getString("X_SWIFI_WRDOFC");
            	String MAIN_NMValue = rs.getString("X_SWIFI_MAIN_NM");
            	String ADRES1Value = rs.getString("X_SWIFI_ADRES1");
                String ADRES2Value = rs.getString("X_SWIFI_ADRES2");
                String INSTL_FLOORValue = rs.getString("X_SWIFI_INSTL_FLOOR");
                String INSTL_TYValue = rs.getString("X_SWIFI_INSTL_TY");
                String INSTL_MBYValue = rs.getString("X_SWIFI_INSTL_MBY");
                String SVC_SEValue = rs.getString("X_SWIFI_SVC_SE");
                String CMCWRValue = rs.getString("X_SWIFI_CMCWR");
                String CNSTC_YEARValue = rs.getString("X_SWIFI_CNSTC_YEAR");
                String INOUT_DOORValue = rs.getString("X_SWIFI_INOUT_DOOR");
                String REMARS3Value = rs.getString("X_SWIFI_REMARS3");
                double LATValue = rs.getDouble("LAT");
                double LNTValue = rs.getDouble("LNT");
                String WORK_DTTMValue = rs.getString("WORK_DTTM");
                
            	double DistanceValue = Math.round(rs.getDouble("DISTANCE")*10)/10000.0;
       	
       
                Wifi2 wifi = new Wifi2();
                wifi.setDISTANCE(DistanceValue);
                wifi.setMGR_NO(MGR_NOValue);
                wifi.setWRDOFC(WRDOFCValue);
                wifi.setMAIN_NM(MAIN_NMValue);
                wifi.setADRES1(ADRES1Value);
                wifi.setADRES2(ADRES2Value);
                wifi.setINSTL_FLOOR(INSTL_FLOORValue);
                wifi.setINSTL_TY(INSTL_TYValue);
                wifi.setINSTL_MBY(INSTL_MBYValue);
                wifi.setSVC_SE(SVC_SEValue);
                wifi.setCMCWR(CMCWRValue);
                wifi.setCNSTC_YEAR(CNSTC_YEARValue);
                wifi.setINOUT_DOOR(INOUT_DOORValue);
                wifi.setREMARS3(REMARS3Value);
                wifi.setLAT(LATValue);
                wifi.setLNT(LNTValue);
                wifi.setWORK_DTTM(WORK_DTTMValue);
                
                
                
                wifiList.add(wifi);
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
        
        return wifiList;
    }

    
    
    public void withdraw(int IDValue) {
        String url = "jdbc:mariadb://192.168.109.128:3306/wifidb";
        String dbUserId = "testuser1";
        String dbPassword = "991021";


        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(url, dbUserId, dbPassword);

            preparedStatement = null;


            String sql = "delete from HISTORY_INFO"
            		+ " where ID = ?;";


            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, IDValue);

            int affected = preparedStatement.executeUpdate();

            if (affected > 0) {
                System.out.println("삭제 성공");
            } else {
                System.out.println("삭제 실패");
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
