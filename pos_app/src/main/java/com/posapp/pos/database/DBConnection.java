package com.posapp.pos.database;

import java.sql.*;

public class DBConnection {
    private final String dBName = "java_pos_app";
    private final String dBUrl = "jdbc:mysql://localhost:3306/" + dBName;
    private final String user = "admin";
    private final String password = "admin";

    private Connection connect;

    public DBConnection() {}

    public Connection getConnection() throws SQLException {
        try{
            connect = DriverManager.getConnection(dBUrl, user, password);
        }catch(SQLException e){
            throw new SQLTimeoutException();
        }
        return connect;
    }

    public void closeConnection(Connection conn, PreparedStatement pst, ResultSet rs) throws SQLException {
        if (conn != null) {
            conn.close();
        }
        if (pst != null) {
            pst.close();
        }
        if (rs != null) {
            rs.close();
        }
    }

    public void closeConnection(Connection conn, PreparedStatement pst) throws SQLException {
        closeConnection(conn, pst, null);
    }

    public void closeConnection(Connection conn, ResultSet rs) throws SQLException {
        closeConnection(conn, null, rs);
    }

    public void closeConnection(Connection conn) throws SQLException {
        closeConnection(conn, null, null);
    }

    public void closeConnection(PreparedStatement pst) throws SQLException {
        closeConnection(null, pst, null);
    }

    public void closeConnection(ResultSet rs) throws SQLException {
        closeConnection(null, null, rs);
    }




}
