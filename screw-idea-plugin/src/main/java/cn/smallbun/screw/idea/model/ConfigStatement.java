package main.java.cn.smallbun.screw.idea.model;

public class ConfigStatement {
    public static DataSourceConfig dataSourceConfig;

    public static DataSourceConfig getDataSourceConfig() {
        return dataSourceConfig;
    }

    public static void setDataSourceConfig(DataSourceConfig dataSourceConfig) {
        ConfigStatement.dataSourceConfig = dataSourceConfig;
    }
}
