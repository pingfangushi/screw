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
import cn.smallbun.screw.core.metadata.Table;
import lombok.Data;

/**
 * 表信息
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/17 20:22
 */
@Data
public class MariadbTableModel implements Table {

    private static final long serialVersionUID = -1917761911812533673L;

    /**
     * tableCat
     */
    @MappingField(value = "TABLE_CAT")
    private String            tableCat;
    /**
     * 表名
     */
    @MappingField(value = "TABLE_NAME")
    private String            tableName;
    /**
     *
     */
    @MappingField(value = "SELF_REFERENCING_COL_NAME")
    private Object            selfReferencingColName;
    /**
     *
     */
    @MappingField(value = "TABLE_CAT")
    private Object            tableSchem;
    /**
     *
     */
    @MappingField(value = "TYPE_SCHEM")
    private Object            typeSchem;
    /**
     *
     */
    @MappingField(value = "TABLE_CAT")
    private Object            typeCat;
    /**
     * 表类型
     */
    @MappingField(value = "TABLE_TYPE")
    private String            tableType;
    /**
     * 备注
     */
    @MappingField(value = "REMARKS")
    private String            remarks;
    /**
     *
     */
    @MappingField(value = "REF_GENERATION")
    private Object            refGeneration;
    /**
     * 类型名称
     */
    @MappingField(value = "TYPE_NAME")
    private Object            typeName;

}
