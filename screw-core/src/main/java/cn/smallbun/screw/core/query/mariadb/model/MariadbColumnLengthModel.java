/*
 * screw-core - 简洁好用的数据库表结构文档生成工具
 * Copyright © 2020 SanLi (qinggang.zuo@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cn.smallbun.screw.core.query.mariadb.model;

import cn.smallbun.screw.core.mapping.MappingField;
import cn.smallbun.screw.core.metadata.ColumnLength;
import lombok.Data;

/**
 * mariadb 列长度
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/8/25 10:56
 */
@Data
public class MariadbColumnLengthModel implements ColumnLength {
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
