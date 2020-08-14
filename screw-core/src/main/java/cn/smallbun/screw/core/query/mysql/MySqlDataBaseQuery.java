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
package cn.smallbun.screw.core.query.mysql;

import cn.smallbun.screw.core.exception.QueryException;
import cn.smallbun.screw.core.mapping.Mapping;
import cn.smallbun.screw.core.metadata.Column;
import cn.smallbun.screw.core.metadata.Database;
import cn.smallbun.screw.core.metadata.PrimaryKey;
import cn.smallbun.screw.core.query.AbstractDatabaseQuery;
import cn.smallbun.screw.core.query.mysql.model.MySqlColumnModel;
import cn.smallbun.screw.core.query.mysql.model.MySqlDatabaseModel;
import cn.smallbun.screw.core.query.mysql.model.MySqlPrimaryKeyModel;
import cn.smallbun.screw.core.query.mysql.model.MySqlTableModel;
import cn.smallbun.screw.core.util.Assert;
import cn.smallbun.screw.core.util.ExceptionUtils;
import cn.smallbun.screw.core.util.JdbcUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static cn.smallbun.screw.core.constant.DefaultConstants.PERCENT_SIGN;

/**
 * mysql 数据库查询
 * <p>
 * 这里需要注意一点，jdbc url 一定要带有 useInformationSchema=true
 * 或者通过配置文件方式配置上,这样才会走 {@link com.mysql.cj.jdbc.DatabaseMetaDataUsingInfoSchema} 元数据查询，查询的数据库表为INFORMATION_SCHEMA
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/18 12:46
 */
public class MySqlDataBaseQuery extends AbstractDatabaseQuery {

    /**
     * 构造函数
     *
     * @param dataSource {@link DataSource}
     */
    public MySqlDataBaseQuery(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * 获取数据库
     *
     * @return {@link Database} 数据库信息
     */
    @Override
    public Database getDataBase() throws QueryException {
        MySqlDatabaseModel model = new MySqlDatabaseModel();
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
    public List<MySqlTableModel> getTables() throws QueryException {
        ResultSet resultSet = null;
        try {
            //查询
            resultSet = getMetaData().getTables(getCatalog(), getSchema(), PERCENT_SIGN,
                new String[] { "TABLE" });
            //映射
            return Mapping.convertList(resultSet, MySqlTableModel.class);
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
    public List<MySqlColumnModel> getTableColumns(String table) throws QueryException {
        String unsigned = "UNSIGNED";
        Assert.notEmpty(table, "Table name can not be empty!");
        ResultSet resultSet = null;
        try {
            //查询
            resultSet = getMetaData().getColumns(getCatalog(), getSchema(), table, PERCENT_SIGN);
            //映射
            List<MySqlColumnModel> list = Mapping.convertList(resultSet, MySqlColumnModel.class);
            //处理长度问题
            for (MySqlColumnModel model : list) {
                // 字段类型
                String dbType = model.getTypeName();
                if (dbType.contains(unsigned)) {
                    // 无符号
                    model.setTypeName(dbType.replace(" UNSIGNED", ""));
                } else {
                    // 有符号
                    if (isNumberType(dbType)) {
                        // 数字类型
                        model.setColumnSize(
                            String.valueOf(Integer.parseInt(model.getColumnSize()) + 1));
                    }
                }
            }
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
        //获取全部列
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
            return Mapping.convertList(resultSet, MySqlPrimaryKeyModel.class);
        } catch (SQLException e) {
            throw ExceptionUtils.mpe(e);
        } finally {
            JdbcUtils.close(resultSet, this.connection);
        }
    }

    /**
     * 根据表名获取主键信息
     *
     * @return {@link List}
     * @throws QueryException QueryException
     */
    @Override
    public List<? extends PrimaryKey> getPrimaryKeys() throws QueryException {
        ResultSet resultSet = null;
        try {
            // 由于单条循环查询存在性能问题，所以这里通过自定义SQL查询数据库主键信息
            String sql = "SELECT TABLE_SCHEMA AS TABLE_CAT, NULL AS TABLE_SCHEM, TABLE_NAME, COLUMN_NAME, SEQ_IN_INDEX AS KEY_SEQ, 'PRIMARY' AS PK_NAME FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = '%s' AND INDEX_NAME = 'PRIMARY' ORDER BY TABLE_SCHEMA, TABLE_NAME, INDEX_NAME, SEQ_IN_INDEX";
            // 拼接参数
            resultSet = prepareStatement(String.format(sql, getDataBase().getDatabase()))
                .executeQuery();
            return Mapping.convertList(resultSet, MySqlPrimaryKeyModel.class);
        } catch (SQLException e) {
            throw new QueryException(e);
        } finally {
            JdbcUtils.close(resultSet);
        }
    }

    protected static boolean isNumberType(String dbType) {
        String[] arr = { "NUMERIC", "DECIMAL", "TINYINT", "SMALLINT", "INTEGER", "BIGINT", "REAL",
                         "FLOAT", "DOUBLE" };
        return Arrays.asList(arr).contains(dbType);
    }
}