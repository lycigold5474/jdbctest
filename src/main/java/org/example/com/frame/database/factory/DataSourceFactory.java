package main.java.org.example.com.frame.database.factory;

import main.java.org.example.com.datamodel.UserInfoBean;

import javax.sql.DataSource;

public class DataSourceFactory {
    private DataSource dataSource = null;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void afterPropertiesSet() throws Exception {
        if (dataSource == null) {
            throw new Exception("datasource is null!!");
        }
    }

    public DataSource getDataSource(UserInfoBean userInfoBean) throws Exception {
        return dataSource;
    }

    public DataSource getDataSource(String dsName) throws Exception {
        return dataSource;
    }
}
