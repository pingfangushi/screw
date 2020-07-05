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
import cn.smallbun.screw.core.metadata.PrimaryKey;
import lombok.Data;

/**
 * 表主键
 *
 * @author <a href ='jxh98@foxmail.com'>Josway</a> 2020/6/28
 * @since JDK 1.8
 */
@Data
public class CacheDbPrimaryKeyModel implements PrimaryKey {

    private static final long serialVersionUID = -4908250184995248600L;
    /**
     *
     */
    @MappingField(value = "TABLE_CATALOG")
    private String            tableCat;
    /**
     * TABLE_NAME
     */
    @MappingField(value = "TABLE_NAME")
    private String            tableName;
    /**
     * TABLE_SCHEM
     */
    @MappingField(value = "TABLE_SCHEM")
    private String            tableSchem;
    /**
     * COLUMN_NAME
     */
    @MappingField(value = "COLUMN_NAME")
    private String            columnName;
    /**
     * KEY_SEQ
     */
    @MappingField(value = "KEY_SEQ")
    private String            keySeq;
    private String            pkName;
}
