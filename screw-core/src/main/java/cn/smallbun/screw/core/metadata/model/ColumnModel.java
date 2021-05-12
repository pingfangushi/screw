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
package cn.smallbun.screw.core.metadata.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 表列领域对象，目前包含如下内容
 * <p>
 * 名称
 * <p>
 * 数据类型
 * <p>
 * 长度
 * <p>
 * 小数位
 * <p>
 * 允许空值
 * <p>
 * 主键
 * <p>
 * 默认值
 * <p>
 * 说明
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/17 20:24
 */
@Data
public class ColumnModel implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -5417752216907437665L;
    /**
     * 表中的列的索引（从 1 开始）
     */
    private String            ordinalPosition;
    /**
     * 名称
     */
    private String            columnName;
    /**
     * SQL 数据类型带长度
     */
    private String            columnType;
    /**
     * SQL 数据类型 名称
     */
    private String            typeName;
    /**
     * 列长度
     */
    private String            columnLength;
    /**
     * 列大小
     */
    private String            columnSize;
    /**
     * 小数位
     */
    private String            decimalDigits;
    /**
     * 可为空
     */
    private String            nullable;
    /**
     * 是否主键
     */
    private String            primaryKey;
    /**
     * 默认值
     */
    private String            columnDef;
    /**
     * 说明
     */
    private String            remarks;

    /**
     * 嵌套数据信息（用于文档数据库）
     */
    private TableModel        nestedTable;

    /**
     * 是否弃用
     */
    private Boolean           deprecated;
}
