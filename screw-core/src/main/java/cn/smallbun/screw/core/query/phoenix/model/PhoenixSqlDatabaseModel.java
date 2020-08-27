package cn.smallbun.screw.core.query.phoenix.model;

import cn.smallbun.screw.core.metadata.Database;
import lombok.Data;

/**
 * @author xielongwang
 * @create 2020/8/19 4:55 下午
 * @email siaron.wang@gmail.com
 * @description
 */
@Data
public class PhoenixSqlDatabaseModel implements Database {
    /**
     * 数据库名称
     */
    private String database;
}
