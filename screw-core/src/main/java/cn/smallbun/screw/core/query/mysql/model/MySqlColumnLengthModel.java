package cn.smallbun.screw.core.query.mysql.model;

import cn.smallbun.screw.core.mapping.MappingField;
import cn.smallbun.screw.core.metadata.ColumnLength;
import lombok.Data;

/**
 * mysql 列长度
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/8/25 10:56
 */
@Data
public class MySqlColumnLengthModel implements ColumnLength {
    /**
     * 表名
     */
    @MappingField(value = "TABLE_NAME")
    private String tableName;

    /**
     * 列名
     */
    @MappingField(value = "COLUMN_NAME")
    private String columnName;

    /**
     * 列长度
     */
    @MappingField(value = "COLUMN_TYPE")
    private String columnType;

    /**
     * 列长度
     *
     * @return {@link String}
     */
    @Override
    public String getColumnLength() {
        String leftParenthesis = "(";
        String rightParenthesis = ")";
        if (columnType.contains(leftParenthesis)) {
            return getColumnType().substring(columnType.indexOf(leftParenthesis) + 1,
                columnType.indexOf(rightParenthesis));
        }
        return "";
    }
}
