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
package cn.smallbun.screw.core.query.phoenix;

import cn.smallbun.screw.core.exception.QueryException;
import cn.smallbun.screw.core.mapping.Mapping;
import cn.smallbun.screw.core.metadata.Database;
import cn.smallbun.screw.core.metadata.PrimaryKey;
import cn.smallbun.screw.core.query.AbstractDatabaseQuery;
import cn.smallbun.screw.core.query.phoenix.model.PhoenixColumnModel;
import cn.smallbun.screw.core.query.phoenix.model.PhoenixPrimaryKeyModel;
import cn.smallbun.screw.core.query.phoenix.model.PhoenixSqlDatabaseModel;
import cn.smallbun.screw.core.query.phoenix.model.PhoenixlTableModel;
import cn.smallbun.screw.core.util.Assert;
import cn.smallbun.screw.core.util.ExceptionUtils;
import cn.smallbun.screw.core.util.JdbcUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static cn.smallbun.screw.core.constant.DefaultConstants.PERCENT_SIGN;

/**
 * @author xielongwang
 * Created by siaron.wang@gmail.com 2020/8/19 4:53 下午
 */
public class PhoenixDataBaseQuery extends AbstractDatabaseQuery {

    public PhoenixDataBaseQuery(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Database getDataBase() throws QueryException {
        PhoenixSqlDatabaseModel model = new PhoenixSqlDatabaseModel();
        model.setDatabase(getCatalog());
        return model;
    }

    @Override
    public List<PhoenixlTableModel> getTables() throws QueryException {
        ResultSet resultSet = null;
        try {
            //查询
            resultSet = getMetaData().getTables("", getSchema(), PERCENT_SIGN,
                new String[] { "TABLE" });
            //映射
            return Mapping.convertList(resultSet, PhoenixlTableModel.class);
        } catch (SQLException e) {
            throw ExceptionUtils.mpe(e);
        } finally {
            JdbcUtils.close(resultSet);
        }
    }

    @Override
    public List<PhoenixColumnModel> getTableColumns(String table) throws QueryException {
        Assert.notEmpty(table, "Table name can not be empty!");
        ResultSet resultSet = null;
        try {
            //查询
            resultSet = getMetaData().getColumns("", getSchema(), table, PERCENT_SIGN);
            //映射
            List<PhoenixColumnModel> list = Mapping.convertList(resultSet,
                PhoenixColumnModel.class);

            //处理columnName
            list.forEach(model -> model.setColumnType(model.getTypeName()));
            return list;
        } catch (SQLException e) {
            throw ExceptionUtils.mpe(e);
        } finally {
            JdbcUtils.close(resultSet);
        }
    }

    @Override
    public List<PhoenixColumnModel> getTableColumns() throws QueryException {
        return getTableColumns(PERCENT_SIGN);
    }

    @Override
    public List<PhoenixPrimaryKeyModel> getPrimaryKeys(String table) throws QueryException {
        ResultSet resultSet = null;
        try {
            //查询
            resultSet = getMetaData().getPrimaryKeys("", getSchema(), table);
            //映射
            return Mapping.convertList(resultSet, PhoenixPrimaryKeyModel.class);
        } catch (SQLException e) {
            throw ExceptionUtils.mpe(e);
        } finally {
            JdbcUtils.close(resultSet, this.connection);
        }
    }

    @Override
    public List<? extends PrimaryKey> getPrimaryKeys() throws QueryException {
        ResultSet resultSet = null;
        try {
            // 由于单条循环查询存在性能问题，所以这里通过自定义SQL查询数据库主键信息
            String sql = "SELECT '' AS TABLE_CAT, TABLE_SCHEM AS TABLE_SCHEM, TABLE_NAME, COLUMN_NAME AS COLUMN_NAME, KEY_SEQ, PK_NAME FROM SYSTEM.\"CATALOG\" WHERE PK_NAME IS NOT NULL AND KEY_SEQ IS NOT NULL GROUP BY TABLE_SCHEM, TABLE_NAME, COLUMN_NAME, KEY_SEQ, PK_NAME";
            // 拼接参数
            resultSet = prepareStatement(sql).executeQuery();
            return Mapping.convertList(resultSet, PhoenixPrimaryKeyModel.class);
        } catch (SQLException e) {
            throw new QueryException(e);
        } finally {
            JdbcUtils.close(resultSet);
        }
    }

    @Override
    protected String getCatalog() throws QueryException {
        return "phoenix";
    }
}
