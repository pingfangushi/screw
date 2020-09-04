package main.java.cn.smallbun.screw.idea.model;

import cn.smallbun.screw.core.engine.EngineFileType;

import java.util.List;

public class DataSourceConfig {

    private String url;

    private String driver;

    private String userName;

    private String password;

    private EngineFileType engineFileType;

    private String fileName;

    private String filePath;

    private Boolean isOpen = false;

    //忽略表
    private List<String> ignoreTable;

    //忽略表前缀
    private List<String> ignorePrefix;

    //忽略表后缀
    private List<String> ignoreSuffix;

    private String desc;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public EngineFileType getEngineFileType() {
        return engineFileType;
    }

    public void setEngineFileType(EngineFileType engineFileType) {
        this.engineFileType = engineFileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Boolean getOpen() {
        return isOpen;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
    }

    public List<String> getIgnoreTable() {
        return ignoreTable;
    }

    public void setIgnoreTable(List<String> ignoreTable) {
        this.ignoreTable = ignoreTable;
    }

    public List<String> getIgnorePrefix() {
        return ignorePrefix;
    }

    public void setIgnorePrefix(List<String> ignorePrefix) {
        this.ignorePrefix = ignorePrefix;
    }

    public List<String> getIgnoreSuffix() {
        return ignoreSuffix;
    }

    public void setIgnoreSuffix(List<String> ignoreSuffix) {
        this.ignoreSuffix = ignoreSuffix;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
