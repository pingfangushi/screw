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
package cn.smallbun.screw.core.query.postgresql;

import cn.smallbun.screw.core.exception.QueryException;
import cn.smallbun.screw.core.mapping.Mapping;
import cn.smallbun.screw.core.metadata.Column;
import cn.smallbun.screw.core.metadata.Database;
import cn.smallbun.screw.core.metadata.PrimaryKey;
import cn.smallbun.screw.core.query.AbstractDatabaseQuery;
import cn.smallbun.screw.core.query.postgresql.model.PostgreSqlColumnModel;
import cn.smallbun.screw.core.query.postgresql.model.PostgreSqlDatabaseModel;
import cn.smallbun.screw.core.query.postgresql.model.PostgreSqlPrimaryKeyModel;
import cn.smallbun.screw.core.query.postgresql.model.PostgreSqlTableModel;
import cn.smallbun.screw.core.util.Assert;
import cn.smallbun.screw.core.util.CollectionUtils;
import cn.smallbun.screw.core.util.ExceptionUtils;
import cn.smallbun.screw.core.util.JdbcUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static cn.smallbun.screw.core.constant.DefaultConstants.PERCENT_SIGN;

/**
 * PostgreSql 查询
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/18 13:58
 */
public class PostgreSqlDataBaseQuery extends AbstractDatabaseQuery {

    /**
     * 构造函数
     *
     * @param dataSource {@link DataSource}
     */
    public PostgreSqlDataBaseQuery(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * 获取数据库
     *
     * @return {@link Database} 数据库信息
     */
    @Override
    public Database getDataBase() throws QueryException {
        PostgreSqlDatabaseModel model = new PostgreSqlDatabaseModel();
        //当前数据库名称
        model.setDatabase(getCatalog());
        return model;
    }

    /**
     * 获取表信息
     *
     * @return {@link List} 所有表信息
     */
    @Override
    public List<PostgreSqlTableModel> getTables() throws QueryException {
        ResultSet resultSet = null;
        try {
            //查询
            resultSet = getMetaData().getTables(getCatalog(), getSchema(), PERCENT_SIGN,
                new String[] { "TABLE" });
            //映射
            return Mapping.convertList(resultSet, PostgreSqlTableModel.class);
        } catch (SQLException e) {
            throw ExceptionUtils.mpe(e);
        } finally {
            JdbcUtils.close(resultSet);
        }

    }

    /**
     * 获取列信息
     *
     * @param table {@link String} 表名
     * @return {@link List} 表字段信息
     */
    @Override
    public List<PostgreSqlColumnModel> getTableColumns(String table) throws QueryException {
        Assert.notEmpty(table, "Table name can not be empty!");
        ResultSet resultSet = null;
        try {
            //查询
            resultSet = getMetaData().getColumns(getCatalog(), getSchema(), table, PERCENT_SIGN);
            //映射
            List<PostgreSqlColumnModel> list = Mapping.convertList(resultSet,
                PostgreSqlColumnModel.class);
            //这里处理是为了如果是查询全部列呢？所以处理并获取唯一表名
            List<String> tableNames = list.stream().map(PostgreSqlColumnModel::getTableName)
                .collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(columnsCaching)) {
                //查询全部
                if (table.equals(PERCENT_SIGN)) {
                    //获取全部表列信息SQL
                    String sql = "SELECT \"TABLE_NAME\", \"TABLE_SCHEMA\", \"COLUMN_NAME\", \"LENGTH\", concat(\"UDT_NAME\", case when \"LENGTH\" isnull then '' else concat('(', concat(\"LENGTH\", ')')) end) \"COLUMN_TYPE\" FROM(select table_schema as \"TABLE_SCHEMA\", column_name as \"COLUMN_NAME\", table_name as \"TABLE_NAME\", udt_name as \"UDT_NAME\", case when coalesce(character_maximum_length, numeric_precision, -1) = -1 then null else coalesce(character_maximum_length, numeric_precision, -1) end as \"LENGTH\" from information_schema.columns a where  table_schema = '%s' and table_catalog = '%s') t";
                    PreparedStatement statement = prepareStatement(
                        String.format(sql, getSchema(), getDataBase().getDatabase()));
                    resultSet = statement.executeQuery();
                    int fetchSize = 4284;
                    if (resultSet.getFetchSize() < fetchSize) {
                        resultSet.setFetchSize(fetchSize);
                    }
                }
                //单表查询
                else {
                    //获取表列信息SQL 查询表名、列名、说明、数据类型
                    String sql = "SELECT \"TABLE_NAME\", \"TABLE_SCHEMA\", \"COLUMN_NAME\", \"LENGTH\", concat(\"UDT_NAME\", case when \"LENGTH\" isnull then '' else concat('(', concat(\"LENGTH\", ')')) end) \"COLUMN_TYPE\" FROM(select table_schema as \"TABLE_SCHEMA\", column_name as \"COLUMN_NAME\", table_name as \"TABLE_NAME\", udt_name as \"UDT_NAME\", case when coalesce(character_maximum_length, numeric_precision, -1) = -1 then null else coalesce(character_maximum_length, numeric_precision, -1) end as \"LENGTH\" from information_schema.columns a where table_name = '%s' and table_schema = '%s' and table_catalog = '%s') t";
                    resultSet = prepareStatement(
                        String.format(sql, table, getSchema(), getDataBase().getDatabase()))
                            .executeQuery();
                }
                List<PostgreSqlColumnModel> inquires = Mapping.convertList(resultSet,
                    PostgreSqlColumnModel.class);
                //处理列，表名为key，列名为值
                tableNames.forEach(name -> columnsCaching.put(name, inquires.stream()
                    .filter(i -> i.getTableName().equals(name)).collect(Collectors.toList())));
            }
            //处理备注信息
            list.forEach(i -> {
                //从缓存中根据表名获取列信息
                List<Column> columns = columnsCaching.get(i.getTableName());
                columns.forEach(j -> {
                    //列名表名一致
                    if (i.getColumnName().equals(j.getColumnName())
                        && i.getTableName().equals(j.getTableName())) {
                        //放入备注
                        i.setColumnLength(j.getColumnLength());
                        i.setColumnType(j.getColumnType());
                    }
                });
            });
            return list;
        } catch (SQLException e) {
            throw ExceptionUtils.mpe(e);
        } finally {
            JdbcUtils.close(resultSet);
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
            return Mapping.convertList(resultSet, PostgreSqlPrimaryKeyModel.class);
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
            String sql = "SELECT result.TABLE_CAT, result.TABLE_SCHEM, result.TABLE_NAME, result.COLUMN_NAME, result.KEY_SEQ, result.PK_NAME FROM(SELECT NULL AS TABLE_CAT, n.nspname AS TABLE_SCHEM, ct.relname AS TABLE_NAME, a.attname AS COLUMN_NAME, (information_schema._pg_expandarray(i.indkey)).n AS KEY_SEQ, ci.relname AS PK_NAME, information_schema._pg_expandarray(i.indkey) AS KEYS, a.attnum AS A_ATTNUM FROM pg_catalog.pg_class ct JOIN pg_catalog.pg_attribute a ON (ct.oid = a.attrelid) JOIN pg_catalog.pg_namespace n ON (ct.relnamespace = n.oid) JOIN pg_catalog.pg_index i ON (a.attrelid = i.indrelid) JOIN pg_catalog.pg_class ci ON (ci.oid = i.indexrelid) WHERE true AND n.nspname = 'public' AND i.indisprimary) result where result.A_ATTNUM = (result.KEYS).x ORDER BY result.table_name, result.pk_name, result.key_seq";
            // 拼接参数
            resultSet = prepareStatement(sql).executeQuery();
            return Mapping.convertList(resultSet, PostgreSqlPrimaryKeyModel.class);
        } catch (SQLException e) {
            throw new QueryException(e);
        } finally {
            JdbcUtils.close(resultSet);
        }
    }
}
