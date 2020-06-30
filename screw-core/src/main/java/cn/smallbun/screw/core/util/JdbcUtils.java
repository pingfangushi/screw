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
package cn.smallbun.screw.core.util;

import cn.smallbun.screw.core.query.DatabaseType;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

/**
 * JDBC工具
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/29 15:48
 */
public class JdbcUtils implements Serializable {

    /**
     * 释放资源
     *
     * @param rs {@link ResultSet}
     */
    public static void close(ResultSet rs) {
        if (!Objects.isNull(rs)) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw ExceptionUtils.mpe(e);
            }
        }
    }

    /**
     * 释放资源
     *
     * @param conn {@link Connection}
     */
    @SuppressWarnings("DuplicatedCode")
    public static void close(Connection conn) {
        if (!Objects.isNull(conn)) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw ExceptionUtils.mpe(e);
            }
        }
    }

    /**
     * 释放资源
     *
     * @param rs   {@link ResultSet}
     * @param conn {@link Connection}
     */
    @SuppressWarnings("DuplicatedCode")
    public static void close(ResultSet rs, Connection conn) {
        if (!Objects.isNull(rs)) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw ExceptionUtils.mpe(e);
            }
        }
        if (!Objects.isNull(conn)) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw ExceptionUtils.mpe(e);
            }
        }
    }

    /**
     * 释放资源
     *
     * @param rs   {@link ResultSet}
     * @param st   {@link Statement}
     * @param conn {@link Connection}
     */
    @SuppressWarnings("DuplicatedCode")
    public static void close(ResultSet rs, Statement st, Connection conn) {
        if (!Objects.isNull(rs)) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw ExceptionUtils.mpe(e);
            }
        }
        if (!Objects.isNull(st)) {
            try {
                st.close();
            } catch (SQLException e) {
                throw ExceptionUtils.mpe(e);
            }
        }
        if (!Objects.isNull(conn)) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw ExceptionUtils.mpe(e);
            }
        }
    }

    /**
     * 根据连接地址判断数据库类型
     *
     * @param jdbcUrl {@link String} 连接地址
     * @return {@link DatabaseType} DatabaseType
     */
    @SuppressWarnings("AlibabaUndefineMagicConstant")
    public static DatabaseType getDbType(String jdbcUrl) {
        if (jdbcUrl.contains(":mysql:") || jdbcUrl.contains(":cobar:")) {
            return DatabaseType.MYSQL;
        } else if (jdbcUrl.contains(":mariadb:")) {
            return DatabaseType.MARIADB;
        } else if (jdbcUrl.contains(":oracle:")) {
            return DatabaseType.ORACLE;
        } else if (jdbcUrl.contains(":sqlserver:") || jdbcUrl.contains(":microsoft:")) {
            return DatabaseType.SQL_SERVER2005;
        } else if (jdbcUrl.contains(":sqlserver2012:")) {
            return DatabaseType.SQL_SERVER;
        } else if (jdbcUrl.contains(":postgresql:")) {
            return DatabaseType.POSTGRE_SQL;
        } else if (jdbcUrl.contains(":hsqldb:")) {
            return DatabaseType.HSQL;
        } else if (jdbcUrl.contains(":db2:")) {
            return DatabaseType.DB2;
        } else if (jdbcUrl.contains(":sqlite:")) {
            return DatabaseType.SQLITE;
        } else if (jdbcUrl.contains(":h2:")) {
            return DatabaseType.H2;
        } else if (jdbcUrl.contains(":dm:")) {
            return DatabaseType.DM;
        } else if (jdbcUrl.contains(":xugu:")) {
            return DatabaseType.XU_GU;
        } else if (jdbcUrl.contains(":kingbase:") || jdbcUrl.contains(":kingbase8:")) {
            return DatabaseType.KINGBASE_ES;
        } else if (jdbcUrl.contains(":phoenix:")) {
            return DatabaseType.PHOENIX;
        } else if (jdbcUrl.contains(":Cache:")) {
            return DatabaseType.CACHEDB;
        } else {
            return DatabaseType.OTHER;
        }
    }
}
