package cn.smallbun.screw.core.query.clickhouse.model;

import cn.smallbun.screw.core.metadata.Database;
import lombok.Data;

/**
 * @author xiaowu
 * 数据库信息
 */
@Data
public class ClickhouseDatabaseModel implements Database {

    private static final long serialVersionUID = -5900809252277746709L;
    /**
     * 数据库名称
     */
    private String            database;
}
