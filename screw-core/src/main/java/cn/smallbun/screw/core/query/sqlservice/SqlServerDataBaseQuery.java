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
package cn.smallbun.screw.core.query.sqlservice;

import cn.smallbun.screw.core.exception.QueryException;
import cn.smallbun.screw.core.mapping.Mapping;
import cn.smallbun.screw.core.metadata.Column;
import cn.smallbun.screw.core.metadata.Database;
import cn.smallbun.screw.core.metadata.PrimaryKey;
import cn.smallbun.screw.core.query.AbstractDatabaseQuery;
import cn.smallbun.screw.core.query.sqlservice.model.SqlServerColumnModel;
import cn.smallbun.screw.core.query.sqlservice.model.SqlServerDatabaseModel;
import cn.smallbun.screw.core.query.sqlservice.model.SqlServerPrimaryKeyModel;
import cn.smallbun.screw.core.query.sqlservice.model.SqlServerTableModel;
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
 * sql server 查询
 * <p> 获取列文档 ：https://docs.microsoft.com/zh-cn/sql/connect/jdbc/reference/getcolumns-method-sqlserverdatabasemetadata?view=sql-server-ver15</p>
 * <p> 获取表文档 ：https://docs.microsoft.com/zh-cn/sql/connect/jdbc/reference/gettables-method-sqlserverdatabasemetadata?view=sql-server-ver15 </p>
 * 通过文档发现，查询列和查询表的 REMARKS  字段SQL Server 不会为此列返回值。所以对于SQL server 自己写SQL语句了,差缺补全
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/18 13:59
 */
@SuppressWarnings("Duplicates")
public class SqlServerDataBaseQuery extends AbstractDatabaseQuery {

    /**
     * 构造函数
     *
     * @param dataSource {@link DataSource}
     */
    public SqlServerDataBaseQuery(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * 获取数据库
     *
     * @return {@link Database} 数据库信息
     */
    @Override
    public Database getDataBase() throws QueryException {
        SqlServerDatabaseModel model = new SqlServerDatabaseModel();
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
    public List<SqlServerTableModel> getTables() {
        ResultSet resultSet = null;
        try {
            //查询
            resultSet = getMetaData().getTables(getCatalog(), getSchema(), PERCENT_SIGN,
                new String[] { "TABLE" });
            //映射
            List<SqlServerTableModel> list = Mapping.convertList(resultSet,
                SqlServerTableModel.class);
            // 由于驱动无法查询出 REMARKS 内容，所以通过自定义SQL查询
            String sql = "select cast(so.name as varchar(500)) as TABLE_NAME, cast(sep.value as varchar(500)) as REMARKS from sysobjects so left JOIN sys.extended_properties sep on sep.major_id = so.id and sep.minor_id = 0 where (xtype = 'U' or xtype = 'v')";
            resultSet = prepareStatement(String.format(sql, getCatalog())).executeQuery();
            List<SqlServerTableModel> inquires = Mapping.convertList(resultSet,
                SqlServerTableModel.class);
            //处理备注信息
            for (SqlServerTableModel model : list) {
                for (SqlServerTableModel inquire : inquires) {
                    if (model.getTableName().equals(inquire.getTableName())) {
                        //备注
                        model.setRemarks(inquire.getRemarks());
                    }
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
     * 获取列信息
     *
     * @param table {@link String} 表名
     * @return {@link List} 表字段信息
     */
    @Override
    public List<SqlServerColumnModel> getTableColumns(String table) throws QueryException {
        Assert.notEmpty(table, "Table name can not be empty!");
        ResultSet resultSet = null;
        try {
            //查询
            resultSet = getMetaData().getColumns(getCatalog(), getSchema(), table, PERCENT_SIGN);
            //映射
            List<SqlServerColumnModel> list = Mapping.convertList(resultSet,
                SqlServerColumnModel.class);
            //这里处理是为了如果是查询全部列呢？所以处理并获取唯一表名
            List<String> tableNames = list.stream().map(SqlServerColumnModel::getTableName)
                .collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(columnsCaching)) {
                //查询全部
                if (table.equals(PERCENT_SIGN)) {
                    //获取全部表列信息SQL
                    String sql = "SELECT cast(a.name AS VARCHAR(500)) AS TABLE_NAME, cast(b.name AS VARCHAR(500)) AS COLUMN_NAME, cast(c.VALUE AS NVARCHAR(500)) AS REMARKS, cast(sys.types.name AS VARCHAR(500)) + '(' + cast(b.max_length AS NVARCHAR(500)) + ')' AS COLUMN_TYPE, cast(b.max_length AS NVARCHAR(500)) AS COLUMN_LENGTH FROM(SELECT name, object_id FROM sys.tables UNION all SELECT name, object_id FROM sys.views) a INNER JOIN sys.columns b ON b.object_id = a.object_id LEFT JOIN sys.types ON b.user_type_id = sys.types.user_type_id LEFT JOIN sys.extended_properties c ON c.major_id = b.object_id AND c.minor_id = b.column_id";
                    PreparedStatement statement = prepareStatement(sql);
                    resultSet = statement.executeQuery();
                    int fetchSize = 4284;
                    if (resultSet.getFetchSize() < fetchSize) {
                        resultSet.setFetchSize(fetchSize);
                    }
                }
                //单表查询
                else {
                    //获取表列信息SQL 查询表名、列名、说明、数据类型
                    String sql = "SELECT cast(a.name AS VARCHAR(500)) AS TABLE_NAME, cast(b.name AS VARCHAR(500)) AS COLUMN_NAME, cast(c.VALUE AS NVARCHAR(500)) AS REMARKS, cast(sys.types.name AS VARCHAR(500)) + '(' + cast(b.max_length AS NVARCHAR(500)) + ')' AS COLUMN_TYPE, cast(b.max_length AS NVARCHAR(500)) AS COLUMN_LENGTH FROM(SELECT name, object_id FROM sys.tables UNION all SELECT name, object_id FROM sys.views) a INNER JOIN sys.columns b ON b.object_id = a.object_id LEFT JOIN sys.types ON b.user_type_id = sys.types.user_type_id LEFT JOIN sys.extended_properties c ON c.major_id = b.object_id AND c.minor_id = b.column_id WHERE a.name = '%s'";
                    resultSet = prepareStatement(String.format(sql, table)).executeQuery();
                }
                List<SqlServerColumnModel> inquires = Mapping.convertList(resultSet,
                    SqlServerColumnModel.class);
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
                        i.setRemarks(j.getRemarks());
                        i.setColumnLength(j.getColumnLength());
                        i.setColumnType(j.getColumnType());
                    }
                });
            });
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
            return Mapping.convertList(resultSet, SqlServerPrimaryKeyModel.class);
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
            // sp_pkeys [ @table_name = ] 'name' [ , [ @table_owner = ] 'owner' ] [ , [ @table_qualifier = ] 'qualifier' ]
            String sql = "SELECT TABLE_CATALOG AS 'TABLE_QUALIFIER', TABLE_SCHEMA AS 'TABLE_OWNER', TABLE_NAME AS 'TABLE_NAME', COLUMN_NAME AS 'COLUMN_NAME', ORDINAL_POSITION AS 'KEY_SEQ', CONSTRAINT_NAME AS 'PK_NAME' FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_CATALOG = '%s' AND TABLE_SCHEMA = '%s' ORDER BY KEY_SEQ";
            // 拼接参数
            resultSet = prepareStatement(String.format(sql, getCatalog(), getSchema()))
                .executeQuery();
            return Mapping.convertList(resultSet, SqlServerPrimaryKeyModel.class);
        } catch (SQLException e) {
            throw new QueryException(e);
        } finally {
            JdbcUtils.close(resultSet);
        }
    }
}
