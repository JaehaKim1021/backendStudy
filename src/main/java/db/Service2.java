package db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Service2{
    //회원등록
    public void addHistoryData(LocationInfo2 locationinfo) {
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
        CallableStatement callableStatement = null;
        ResultSet rs = null;



        try {
            connection = DriverManager.getConnection(url, dbUserId, dbPassword);


                preparedStatement = null;


                String sql = "insert HISTORY_INFO(LAT, LNT, SEARCH_DATE)"
                		+ " value (?, ?, now());";


                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setDouble(1, locationinfo.getLat());
                preparedStatement.setDouble(2, locationinfo.getLnt());

                int affected = preparedStatement.executeUpdate();

                if (affected > 0) {
                	System.out.println("저장 성공");
                } else {
                    System.out.println("저장 실패");
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