/*
 * screw-core - 简洁好用的数据库文档生成器
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
import cn.smallbun.screw.core.metadata.PrimaryKey;
import cn.smallbun.screw.core.util.Assert;
import cn.smallbun.screw.core.util.ExceptionUtils;
import cn.smallbun.screw.core.util.StringUtils;
import lombok.Getter;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static cn.smallbun.screw.core.constant.DefaultConstants.NOT_SUPPORTED;

/**
 * AbstractDataBaseQuery
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/18 12:50
 */
public abstract class AbstractDatabaseQuery implements DatabaseQuery {
    /**
     * DataSource
     */
    @Getter
    private final DataSource      dataSource;

    /**
     * Connection 双重锁，线程安全
     */
    volatile protected Connection connection;

    public AbstractDatabaseQuery(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 获取连接对象，单例模式，采用双重锁检查
     *
     * @return {@link Connection}
     * @throws QueryException QueryException
     */
    private Connection getConnection() throws QueryException {
        try {
            //不为空
            if (!Objects.isNull(connection) && !connection.isClosed()) {
                return connection;
            }
            //同步代码块
            synchronized (AbstractDatabaseQuery.class) {
                //为空或者已经关闭
                if (Objects.isNull(connection) || connection.isClosed()) {
                    this.connection = this.getDataSource().getConnection();
                }
            }
            return this.connection;
        } catch (SQLException e) {
            throw ExceptionUtils.mpe(e);
        }
    }

    /**
     * 获取 getCatalog
     *
     * @return {@link String}
     * @throws QueryException QueryException
     */
    protected String getCatalog() throws QueryException {
        try {
            String catalog = this.getConnection().getCatalog();
            if (StringUtils.isBlank(catalog)) {
                return null;
            }
            return catalog;
        } catch (SQLException e) {
            throw ExceptionUtils.mpe(e);
        }
    }

    /**
     * 获取 getSchema
     *
     * @return {@link String}
     * @throws QueryException QueryException
     */
    protected String getSchema() throws QueryException {
        try {
            String schema = this.getConnection().getSchema();
            if (StringUtils.isBlank(schema)) {
                return null;
            }
            return schema;
        } catch (SQLException e) {
            throw ExceptionUtils.mpe(e);
        }
    }

    /**
     * 获取 DatabaseMetaData
     *
     * @return {@link DatabaseMetaData}
     * @throws QueryException QueryException
     */
    protected DatabaseMetaData getMetaData() throws QueryException {
        try {
            return this.getConnection().getMetaData();
        } catch (SQLException e) {
            throw ExceptionUtils.mpe(e);
        }
    }

    /**
     * 准备声明
     *
     * @param sql {@link String} SQL
     * @return {@link PreparedStatement}
     * @throws QueryException QueryException
     */
    protected PreparedStatement prepareStatement(String sql) throws QueryException {
        Assert.notEmpty(sql, "Sql can not be empty!");
        try {
            return this.getConnection().prepareStatement(sql);
        } catch (SQLException e) {
            throw ExceptionUtils.mpe(e);
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
        throw ExceptionUtils.mpe(NOT_SUPPORTED);
    }
}
