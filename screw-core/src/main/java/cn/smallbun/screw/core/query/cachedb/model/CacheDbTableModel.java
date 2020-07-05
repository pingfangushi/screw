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
package cn.smallbun.screw.core.query.cachedb.model;

import cn.smallbun.screw.core.mapping.MappingField;
import cn.smallbun.screw.core.metadata.Table;
import lombok.Data;

/**
 * 表信息
 *
 * @author <a href ='jxh98@foxmail.com'>Josway</a> 2020/6/28
 * @since JDK 1.8
 */
@Data
public class CacheDbTableModel implements Table {

    private static final long serialVersionUID = -1917761911812533673L;
    /**
     *
     */
    @MappingField(value = "TABLE_CAT")
    private String            tableCat;
    /**
     * TABLE_NAME
     */
    @MappingField(value = "TABLE_NAME")
    private String            tableName;
    /**
     *
     */
    @MappingField(value = "SELF_REFERENCING_COL_NAME")
    private String            selfReferencingColName;
    /**
     * TABLE_SCHEM
     */
    @MappingField(value = "TABLE_SCHEM")
    private String            tableSchem;
    /**
     *
     */
    @MappingField(value = "TYPE_SCHEM")
    private String            typeSchem;
    /**
     *
     */
    @MappingField(value = "TYPE_CAT")
    private Object            typeCat;
    /**
     * TABLE_TYPE
     */
    @MappingField(value = "TABLE_TYPE")
    private String            tableType;
    /**
     * REMARKS
     */
    @MappingField(value = "REMARKS")
    private String            remarks;
    /**
     *
     */
    @MappingField(value = "REF_GENERATION")
    private String            refGeneration;
    /**
     *
     */
    @MappingField(value = "TYPE_NAME")
    private String            typeName;
}
