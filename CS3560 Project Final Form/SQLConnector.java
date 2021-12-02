package main;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

public class SQLConnector  {
    private String dbURL;
    private String user;
    private String pass;

    public SQLConnector(String url, String username, String password) {
        this.dbURL = url;
        this.user = username;
        this.pass = password;
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(dbURL, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    public CachedRowSet query(String query) throws SQLException{
        Connection conn = connect();
        ResultSet rs = null;


        Statement stmt = conn.createStatement();
        rs = stmt.executeQuery(query);

        RowSetFactory factory = RowSetProvider.newFactory();
        CachedRowSet rowset = factory.createCachedRowSet();
        rowset.populate(rs);
        if(rs != null)
            try {rs.close();} catch (SQLException e){e.printStackTrace();}
        if(stmt!= null)
            try {stmt.close();} catch (SQLException e){e.printStackTrace();}
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return rowset;
    }

    public int update(String query) throws SQLException{
        Connection conn = connect();
        int rowsUpdated = 0;

        Statement stmt = conn.createStatement();
        rowsUpdated = stmt.executeUpdate(query);

        if(stmt!= null)
            try {stmt.close();} catch (SQLException e){e.printStackTrace();}
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return rowsUpdated;
    }

    public void getServerInfo()  throws SQLException {
        Connection conn = connect();

        if (conn != null) {
            DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
            System.out.println("Database name: " + conn.getCatalog());
            System.out.println("Driver name: " + dm.getDriverName());
            System.out.println("Driver version: " + dm.getDriverVersion());
            System.out.println("Product name: " + dm.getDatabaseProductName());
            System.out.println("Product version: " + dm.getDatabaseProductVersion());
        }

        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
