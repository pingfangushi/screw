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
package cn.smallbun.screw.core.query.db2;

import cn.smallbun.screw.core.exception.QueryException;
import cn.smallbun.screw.core.mapping.Mapping;
import cn.smallbun.screw.core.metadata.Column;
import cn.smallbun.screw.core.metadata.Database;
import cn.smallbun.screw.core.metadata.PrimaryKey;
import cn.smallbun.screw.core.metadata.Table;
import cn.smallbun.screw.core.query.AbstractDatabaseQuery;
import cn.smallbun.screw.core.query.db2.model.Db2ColumnModel;
import cn.smallbun.screw.core.query.db2.model.Db2DatabaseModel;
import cn.smallbun.screw.core.query.db2.model.Db2PrimaryKeyModel;
import cn.smallbun.screw.core.query.db2.model.Db2TableModel;
import cn.smallbun.screw.core.query.mysql.model.MySqlTableModel;
import cn.smallbun.screw.core.query.oracle.model.OracleColumnModel;
import cn.smallbun.screw.core.query.oracle.model.OraclePrimaryKeyModel;
import cn.smallbun.screw.core.query.oracle.model.OracleTableModel;
import cn.smallbun.screw.core.util.Assert;
import cn.smallbun.screw.core.util.CollectionUtils;
import cn.smallbun.screw.core.util.ExceptionUtils;
import cn.smallbun.screw.core.util.JdbcUtils;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.smallbun.screw.core.constant.DefaultConstants.NOT_SUPPORTED;
import static cn.smallbun.screw.core.constant.DefaultConstants.PERCENT_SIGN;

/**
 * db2 数据库查询
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/18 14:00
 */
public class Db2DataBaseQuery extends AbstractDatabaseQuery {
    /**
     * 构造函数
     *
     * @param dataSource {@link DataSource}
     */
    public Db2DataBaseQuery(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * 获取数据库
     *
     * @return {@link Database} 数据库信息
     */
    @Override
    public Database getDataBase() throws QueryException {
        Db2DatabaseModel model = new Db2DatabaseModel();
        model.setDatabase(StringUtils.trim(this.getSchema()));
        return model;
    }

    /**
     * 获取表信息
     *
     * @return {@link List} 所有表信息
     */
    @Override
    public List<Db2TableModel> getTables() {
        ResultSet resultSet = null;
        try {
            //查询
            resultSet = getMetaData().getTables(getCatalog(), getSchema(), PERCENT_SIGN,
                new String[] { "TABLE" });
            //映射
            return Mapping.convertList(resultSet, Db2TableModel.class);
        } catch (SQLException e) {
            throw ExceptionUtils.mpe(e);
        } finally {
            JdbcUtils.close(resultSet, connection);
        }
    }

    /**
     * 获取列信息
     *
     * @param table {@link String} 表名
     * @return {@link List} 表字段信息
     * @throws QueryException QueryException
     */
    @Override
    public List<Db2ColumnModel> getTableColumns(String table) throws QueryException {
        Assert.notEmpty(table, "Table name can not be empty!");
        ResultSet resultSet = null;
        try {
            //查询所有
            List<Db2ColumnModel> list;
            if (PERCENT_SIGN.equals(table)) {
                List<String> tableNames = getTables().stream().map(Table::getTableName)
                    .collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
                //db2 getMetaData().getColumns 中 无法获取表名,循环单张表获取
                list = new ArrayList<>();
                for (String tableName : tableNames) {
                    list.addAll(getTableColumns(tableName));
                }
            } else {
                if (!this.columnsCaching.containsKey(table)) {
                    //查询
                    resultSet = getMetaData().getColumns(getCatalog(), getSchema(), table,
                        PERCENT_SIGN);
                    //映射
                    list = Mapping.convertList(resultSet, Db2ColumnModel.class);
                    //单表查询
                    String sql = "SELECT COLNAME as NAME,TABNAME as TABLE_NAME,REMARKS,LENGTH as COLUMN_LENGTH, TYPENAME ||'('|| LENGTH ||')'  as COLUMN_TYPE,SCALE as DECIMAL_DIGITS   FROM SYSCAT.COLUMNS WHERE TABSCHEMA='"
                                 + StringUtils.trim(getSchema())
                                 + "' and TABNAME = '%s' ORDER BY COLNO";
                    resultSet = prepareStatement(String.format(sql, table)).executeQuery();
                    //db2 ColumnName 获取的不是 as column 值,使用ColumnLabel获取
                    List<Db2ColumnModel> inquires = Mapping.convertListByColumnLabel(resultSet,
                        Db2ColumnModel.class);
                    for (Db2ColumnModel i : list) {
                        inquires.forEach(j -> {
                            //列名一致
                            if (i.getColumnName().equals(j.getColumnName())) {
                                //放入备注
                                i.setRemarks(j.getRemarks());
                                i.setColumnLength(j.getColumnLength());
                                i.setColumnType(j.getColumnType());
                                i.setTableName(j.getTableName());
                                i.setDecimalDigits(j.getDecimalDigits());
                                if ("NO".equals(i.getNullable())) {
                                    i.setNullable("0");
                                }
                            }
                        });
                    }
                    this.columnsCaching.put(table,
                        list.stream().filter(i -> StringUtils.isNotBlank(i.getColumnName()))
                            .collect(Collectors.toList()));
                } else {
                    list = this.columnsCaching.get(table).stream().map(c -> (Db2ColumnModel) c)
                        .collect(Collectors.toList());
                }
            }
            return list;
        } catch (SQLException e) {
            throw ExceptionUtils.mpe(e);
        } finally {
            JdbcUtils.close(resultSet, this.connection);
        }
    }

    /**
     * 获取所有列信息
     *
     * @return {@link List} 表字段信息
     * @throws QueryException QueryException
     */
    @Override
    public List<? extends Column> getTableColumns() throws QueryException {
        return getTableColumns(PERCENT_SIGN);
    }

    /**
     * 根据表名获取主键
     *
     * @param table {@link String}
     * @return {@link List}
     * @throws QueryException QueryException
     */
    @Override
    public List<? extends PrimaryKey> getPrimaryKeys(String table) throws QueryException {
        ResultSet resultSet = null;
        try {
            //查询
            resultSet = getMetaData().getPrimaryKeys(getCatalog(), getSchema(), table);
            //映射
            return Mapping.convertList(resultSet, Db2PrimaryKeyModel.class);
        } catch (SQLException e) {
            throw ExceptionUtils.mpe(e);
        } finally {
            JdbcUtils.close(resultSet, this.connection);
        }
    }

    /**
     * 根据表名获取主键
     *
     * @return {@link List}
     * @throws QueryException QueryException
     */
    @Override
    public List<? extends PrimaryKey> getPrimaryKeys() throws QueryException {
        ResultSet resultSet = null;
        try {
            // 由于单条循环查询存在性能问题，所以这里通过自定义SQL查询数据库主键信息
            String sql = "SELECT TABSCHEMA as TABLE_SCHEM, TABNAME as TABLE_NAME ,COLNAME as COLUMN_NAME,KEYSEQ as KEY_SEQ   FROM SYSCAT.COLUMNS WHERE TABSCHEMA = '%s' AND KEYSEQ IS NOT NULL ORDER BY KEYSEQ";
            resultSet = prepareStatement(String.format(sql, getDataBase().getDatabase()))
                .executeQuery();
            return Mapping.convertListByColumnLabel(resultSet, Db2PrimaryKeyModel.class);
        } catch (SQLException e) {
            throw new QueryException(e);
        } finally {
            JdbcUtils.close(resultSet);
        }
    }

}
