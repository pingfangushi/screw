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
package cn.smallbun.screw.core.query.oracle;

import cn.smallbun.screw.core.exception.QueryException;
import cn.smallbun.screw.core.mapping.Mapping;
import cn.smallbun.screw.core.metadata.Column;
import cn.smallbun.screw.core.metadata.Database;
import cn.smallbun.screw.core.metadata.PrimaryKey;
import cn.smallbun.screw.core.query.AbstractDatabaseQuery;
import cn.smallbun.screw.core.query.oracle.model.OracleColumnModel;
import cn.smallbun.screw.core.query.oracle.model.OracleDatabaseModel;
import cn.smallbun.screw.core.query.oracle.model.OraclePrimaryKeyModel;
import cn.smallbun.screw.core.query.oracle.model.OracleTableModel;
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
 * oracle 数据库查询
 * <p>
 * 还是采用从驱动中拿到数据的方式，这里注意一点，一定要加入配置参数remarks为true 否则表和列等说明不会查询出来
 * hikari：
 * config.addDataSourceProperty("remarks", "true");
 * <p>
 * 不过这种查询性能很慢 https://docs.oracle.com/en/database/oracle/oracle-database/20/jjdbc/performance-extensions.html#GUID-15865071-39F2-430F-9EDA-EB34D0B2D560
 * 所以，只能够通过自定义SQL来了
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/18 13:58
 */
@SuppressWarnings("Duplicates")
public class OracleDataBaseQuery extends AbstractDatabaseQuery {
    /**
     * 构造函数
     *
     * @param dataSource {@link DataSource}
     */
    public OracleDataBaseQuery(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * 获取数据库
     *
     * @return {@link Database} 数据库信息
     */
    @Override
    public Database getDataBase() throws QueryException {
        OracleDatabaseModel model = new OracleDatabaseModel();
        //当前数据库名称
        model.setDatabase(getSchema());
        return model;
    }

    /**
     * 获取表信息
     *
     * @return {@link List} 所有表信息
     */
    @Override
    public List<OracleTableModel> getTables() throws QueryException {
        ResultSet resultSet = null;
        try {
            //查询
            resultSet = getMetaData().getTables(getCatalog(), getSchema(), PERCENT_SIGN,
                new String[] { "TABLE" });
            //映射
            List<OracleTableModel> list = Mapping.convertList(resultSet, OracleTableModel.class);
            //由于ORACLE 查询 REMARKS 非常耗费性能，所以这里使用自定义SQL查询
            //https://docs.oracle.com/en/database/oracle/oracle-database/20/jjdbc/performance-extensions.html#GUID-15865071-39F2-430F-9EDA-EB34D0B2D560
            //获取所有表 查询表名、说明
            String sql = "SELECT TABLE_NAME,COMMENTS AS REMARKS FROM USER_TAB_COMMENTS WHERE TABLE_TYPE = 'TABLE'";
            if (isDda()) {
                //DBA 使用 DBA_TAB_COMMENTS 进行查询 Oracle连接用户和schema不同问题。dba连接用户可以生成不同schema下的表结构
                sql = "SELECT TABLE_NAME,COMMENTS AS REMARKS FROM DBA_TAB_COMMENTS WHERE TABLE_TYPE = 'TABLE' AND OWNER = '"
                      + getSchema() + "'";
            }
            resultSet = prepareStatement(String.format(sql, getSchema())).executeQuery();
            List<OracleTableModel> inquires = Mapping.convertList(resultSet,
                OracleTableModel.class);
            //处理备注信息
            list.forEach((OracleTableModel model) -> {
                //备注
                inquires.stream()
                    .filter(inquire -> model.getTableName().equals(inquire.getTableName()))
                    .forEachOrdered(inquire -> model.setRemarks(inquire.getRemarks()));
            });
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
    public List<OracleColumnModel> getTableColumns(String table) throws QueryException {
        Assert.notEmpty(table, "Table name can not be empty!");
        ResultSet resultSet = null;
        try {
            //查询
            resultSet = getMetaData().getColumns(getCatalog(), getSchema(), table, PERCENT_SIGN);
            //映射
            List<OracleColumnModel> list = Mapping.convertList(resultSet, OracleColumnModel.class);
            //由于ORACLE 查询 COLUMNS REMARKS 为NULL，所以这里使用自定义SQL查询
            //https://docs.oracle.com/en/database/oracle/oracle-database/20/jjdbc/performance-extensions.html#GUID-15865071-39F2-430F-9EDA-EB34D0B2D560
            List<String> tableNames = list.stream().map(OracleColumnModel::getTableName)
                .collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(columnsCaching)) {
                //查询全部
                if (table.equals(PERCENT_SIGN)) {
                    String sql = "SELECT ut.TABLE_NAME,  ut.COLUMN_NAME, uc.comments as REMARKS, concat(concat(concat(ut.DATA_TYPE, '('), ut.DATA_LENGTH), ')') AS COLUMN_TYPE, ut.DATA_LENGTH as COLUMN_LENGTH FROM user_tab_columns ut INNER JOIN user_col_comments uc ON ut.TABLE_NAME = uc.table_name AND ut.COLUMN_NAME = uc.column_name";
                    if (isDda()) {
                        sql = "SELECT ut.TABLE_NAME,  ut.COLUMN_NAME, uc.comments as REMARKS, concat(concat(concat(ut.DATA_TYPE, '('), ut.DATA_LENGTH), ')') AS COLUMN_TYPE, ut.DATA_LENGTH as COLUMN_LENGTH FROM dba_tab_columns ut INNER JOIN dba_col_comments uc ON ut.TABLE_NAME = uc.table_name AND ut.COLUMN_NAME = uc.column_name and ut.OWNER = uc.OWNER WHERE ut.OWNER = '"
                              + getDataBase() + "'";
                    }
                    PreparedStatement statement = prepareStatement(sql);
                    resultSet = statement.executeQuery();
                    int fetchSize = 4284;
                    if (resultSet.getFetchSize() < fetchSize) {
                        resultSet.setFetchSize(fetchSize);
                    }
                }
                //单表查询
                else {
                    String sql = "SELECT ut.TABLE_NAME,  ut.COLUMN_NAME, uc.comments as REMARKS, concat(concat(concat(ut.DATA_TYPE, '('), ut.DATA_LENGTH), ')') AS COLUMN_TYPE, ut.DATA_LENGTH as COLUMN_LENGTH FROM user_tab_columns ut INNER JOIN user_col_comments uc ON ut.TABLE_NAME = uc.table_name AND ut.COLUMN_NAME = uc.column_name WHERE ut.Table_Name = '%s'";
                    if (isDda()) {
                        sql = "SELECT ut.TABLE_NAME,  ut.COLUMN_NAME, uc.comments as REMARKS, concat(concat(concat(ut.DATA_TYPE, '('), ut.DATA_LENGTH), ')') AS COLUMN_TYPE, ut.DATA_LENGTH as COLUMN_LENGTH FROM dba_tab_columns ut INNER JOIN dba_col_comments uc ON ut.TABLE_NAME = uc.table_name AND ut.COLUMN_NAME = uc.column_name and ut.OWNER = uc.OWNER WHERE ut.Table_Name = '%s' ut.OWNER = '"
                              + getDataBase() + "'";
                    }
                    resultSet = prepareStatement(String.format(sql, table)).executeQuery();
                }
                List<OracleColumnModel> inquires = Mapping.convertList(resultSet,
                    OracleColumnModel.class);
                //处理列，表名为key，列名为值
                tableNames.forEach(name -> columnsCaching.put(name, inquires.stream()
                    .filter(i -> i.getTableName().equals(name)).collect(Collectors.toList())));
            }
            //处理备注信息
            //从缓存中根据表名获取列信息
            for (OracleColumnModel i : list) {
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
            return Mapping.convertList(resultSet, OraclePrimaryKeyModel.class);
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
            String sql = "SELECT NULL AS TABLE_CAT, C.OWNER AS TABLE_SCHEM, C.TABLE_NAME, C.COLUMN_NAME, C.POSITION AS KEY_SEQ, C.CONSTRAINT_NAME AS PK_NAME FROM ALL_CONS_COLUMNS C, ALL_CONSTRAINTS K WHERE K.CONSTRAINT_TYPE = 'P' AND K.OWNER LIKE '%s' ESCAPE '/' AND K.CONSTRAINT_NAME = C.CONSTRAINT_NAME AND K.TABLE_NAME = C.TABLE_NAME AND K.OWNER = C.OWNER ORDER BY COLUMN_NAME ";
            // 拼接参数
            resultSet = prepareStatement(String.format(sql, getDataBase().getDatabase()))
                .executeQuery();
            return Mapping.convertList(resultSet, OraclePrimaryKeyModel.class);
        } catch (SQLException e) {
            throw new QueryException(e);
        } finally {
            JdbcUtils.close(resultSet);
        }
    }

    /**
     *
     * 当前用户是否为DBA
     *
     * @return {@link Boolean}
     */
    private boolean isDda() {
        ResultSet resultSet = null;
        try {
            //判断是否是DBA
            resultSet = prepareStatement("SELECT USERENV('isdba') as IS_DBA FROM DUAL")
                .executeQuery();
            String dbaColumn = "IS_DBA";
            resultSet.next();
            return resultSet.getBoolean(dbaColumn);
        } catch (SQLException e) {
            throw new QueryException(e);
        } finally {
            JdbcUtils.close(resultSet);
        }
    }
}
