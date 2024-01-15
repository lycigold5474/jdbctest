package main.java.org.example.com.frame.database.factory;

import main.java.org.example.com.datamodel.UserInfoBean;
import main.java.org.example.com.frame.database.util.DbConstants;

import javax.sql.DataSource;
import java.sql.Connection;

public class ConnectionFactory {
    private DataSource dataSource = null;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void afterPropertiesSet() throws Exception {
        if (dataSource == null) {
            throw new Exception("datasource is null!!");
        }
    }

    public Connection getConnection() throws Exception {
        return getConnection(DbConstants.DS_NAME);
    }

    public Connection getConnection(String dsName) throws Exception{
        Connection conn = null;

        try {
            if(DbConstants.DS_NAME.equals(dsName)) {
                conn = dataSource.getConnection();
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }

        return conn;
    }

    public Connection getDataSource(UserInfoBean infoBean) throws Exception{
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }

        return conn;
    }
}
