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
package cn.smallbun.screw.core.query;

import cn.smallbun.screw.core.exception.QueryException;
import cn.smallbun.screw.core.metadata.Column;
import cn.smallbun.screw.core.metadata.Database;
import cn.smallbun.screw.core.metadata.PrimaryKey;
import cn.smallbun.screw.core.metadata.Table;

import java.io.Serializable;
import java.util.List;

/**
 * 通用查询接口
 * 查询数据库信息
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/18 11:58
 */
public interface DatabaseQuery extends Serializable {
    /**
     * 获取数据库
     *
     * @return {@link Database} 数据库信息
     * @throws QueryException QueryException
     */
    Database getDataBase() throws QueryException;

    /**
     * 获取表信息
     *
     * @return {@link List} 所有表信息
     * @throws QueryException QueryException
     */
    List<? extends Table> getTables() throws QueryException;

    /**
     * 获取列信息
     *
     * @param table {@link String} 表名
     * @return {@link List} 表字段信息
     * @throws QueryException QueryException
     */
    List<? extends Column> getTableColumns(String table) throws QueryException;

    /**
     * 获取所有列信息
     *
     * @return {@link List} 表字段信息
     * @throws QueryException QueryException
     */
    List<? extends Column> getTableColumns() throws QueryException;

    /**
     * 根据表名获取主键
     *
     * @param table {@link String}
     * @return {@link List}
     * @throws QueryException QueryException
     */
    List<? extends PrimaryKey> getPrimaryKeys(String table) throws QueryException;

    /**
     * 获取主键
     *
     * @return {@link List}
     * @throws QueryException QueryException
     */
    List<? extends PrimaryKey> getPrimaryKeys() throws QueryException;
}
