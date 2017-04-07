package com.example.reda_benchraa.asn.DB;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class DataAccess {
    private static final String FILE_PROPERTIES   = "dataAccess.properties";
    private static final String PROPERTY_DRIVER   = "DB_DRIVER";
    private static final String PROPERTY_URL      = "DB_URL";
    private static final String PROPERTY_NAME     = "DB_NAME";
    private static final String PROPERTY_USER     = "DB_USER";
    private static final String PROPERTY_PASSWORD = "DB_PASS";

    public String db_url;
    public static String db_name;
    public String db_user;
    public String db_pass;

    public static void setDbname(String name){
        db_name = name;
    }

    public DataAccess(String db_url, String db_name,String db_user, String db_pass) {
        this.db_url = db_url;
        this.db_user = db_user;
        this.db_pass = db_pass;
    }
    public static DataAccess getInstance() throws DataAccessException {

        Properties properties = new Properties();
        properties.setProperty(PROPERTY_URL,"jdbc:mysql://localhost/");
        properties.setProperty(PROPERTY_NAME,"ASN");
        properties.setProperty(PROPERTY_DRIVER,"com.mysql.jdbc.Driver");
        properties.setProperty(PROPERTY_USER,"root");
        properties.setProperty(PROPERTY_PASSWORD,"");
        /*ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream propertiesFile= classLoader.getResourceAsStream(FILE_PROPERTIES);
        try {
            properties.load(propertiesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (propertiesFile == null) {
            throw new DataAccessException("File " + FILE_PROPERTIES + " not found.");
        }*/


        String url=properties.getProperty(PROPERTY_URL);
        String driver=properties.getProperty(PROPERTY_DRIVER);
        String name=properties.getProperty(PROPERTY_NAME);
        String user=properties.getProperty(PROPERTY_USER);
        String password=properties.getProperty(PROPERTY_PASSWORD);
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new DataAccessException("Driver not found.", e);
        }
        DataAccess instance = new DataAccess(url, name, user, password);
        return instance;
    }
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(db_url + db_name, db_user, db_pass);
    }

    public static int executeSQL(Connection conn, String sql, Object...params) throws DataAccessException{
        int rows = 0;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++)
                stmt.setObject(i + 1, params[i]);
            rows = stmt.executeUpdate();

        } catch (Exception e) {
            throw new DataAccessException(e);
        }

        return rows;
    }
    public static ResultSet select(Connection conn, String sql, Object...params) throws DataAccessException, SQLException{
        ResultSet rs = null;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++)
                stmt.setObject(i + 1, params[i]);

            rs = stmt.executeQuery();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
        return rs;
    }
}
