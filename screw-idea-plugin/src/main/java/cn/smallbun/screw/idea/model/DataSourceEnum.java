package main.java.cn.smallbun.screw.idea.model;

public enum DataSourceEnum {
    /**
     * Mysql
     * Oracle
     * MariaDB
     * TIDB
     * SqlServer
     * PostgreSQL
     * Cache DB
     */
    Mysql("mysql","com.mysql.jdbc.Driver"),
    Oracle("oracle","oracle.jdbc.driver.OracleDriver"),MariaDB("MariaDB","org.mariadb.jdbc.Driver"),
    TIDB("TIDB","com.mysql.jdbc.Driver"),SqlServer("SqlServer","com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    PostgreSQL("PostgreSQL","org.postgresql.Driver")
    /*,CacheDB("CacheDB","")*/;


    DataSourceEnum(String database, String driverClass) {
        this.database = database;
        this.driverClass = driverClass;
    }


    private String database;

    private String driverClass;

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public static String getDatabase(String database) {
        DataSourceEnum[] dataSourceEnums = DataSourceEnum.values();
        for (DataSourceEnum dataSourceEnum : dataSourceEnums) {
            if (dataSourceEnum.getDatabase().equalsIgnoreCase(database)) {
                return dataSourceEnum.getDriverClass();
            }
        }

        return null;

    }
}
