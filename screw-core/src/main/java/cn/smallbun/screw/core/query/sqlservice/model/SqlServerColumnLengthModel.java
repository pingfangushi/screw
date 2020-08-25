package cn.smallbun.screw.core.query.sqlservice.model;

import cn.smallbun.screw.core.metadata.ColumnLength;
import lombok.Data;

/**
 * sqlserver 列长度
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/8/25 10:56
 */
@Data
public class SqlServerColumnLengthModel implements ColumnLength {
    /**
     * 表名
     */
    private String tableName;

    /**
     * 列名
     */
    private String columnName;

    /**
     * 列长度
     */
    private String columnLength;
}
