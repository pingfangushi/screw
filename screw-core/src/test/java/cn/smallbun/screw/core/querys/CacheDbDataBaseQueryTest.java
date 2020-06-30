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
package cn.smallbun.screw.core.querys;

import cn.smallbun.screw.core.common.Properties;
import cn.smallbun.screw.core.exception.QueryException;
import cn.smallbun.screw.core.metadata.Column;
import cn.smallbun.screw.core.metadata.Database;
import cn.smallbun.screw.core.metadata.PrimaryKey;
import cn.smallbun.screw.core.metadata.Table;
import cn.smallbun.screw.core.query.DatabaseQuery;
import cn.smallbun.screw.core.query.DatabaseQueryFactory;
import com.alibaba.fastjson.JSON;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * CacheDB 测试
 *
 * @author <a href ='jxh98@foxmail.com'>Josway</a>
 * @date 2020/6/28
 * @since JDK 1.8
 */
public class CacheDbDataBaseQueryTest implements Properties {

    /**
     * 日志
     */
    private final Logger  logger = LoggerFactory.getLogger(this.getClass());
    /**
     * DataBaseQuery
     */
    private DatabaseQuery query;

    /**
     * before
     */
    @BeforeEach
    void before() throws IOException {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(getDriver());
        config.setJdbcUrl(getUrl());
        config.setUsername(getUserName());
        config.setPassword(getPassword());
        config.setSchema(getSchema());
        config.setPoolName(UUID.randomUUID().toString().replace("-", ""));
        DataSource dataSource = new HikariDataSource(config);
        query = new DatabaseQueryFactory(dataSource).newInstance();
    }

    /**
     * 查询数据库
     */
    @Test
    void databases() throws QueryException {
        Database dataBasesInfo = query.getDataBase();
        logger.info(JSON.toJSONString(dataBasesInfo, true));
    }

    /**
     * 查询表
     */
    @Test
    void tables() throws QueryException {
        query.getDataBase();
        List<? extends Table> tables = query.getTables();
        logger.info(JSON.toJSONString(tables, true));
    }

    /**
     * 查询列
     */
    @Test
    void column() throws QueryException {
        List<? extends Column> columns = query.getTableColumns("demo_data");
        logger.info(JSON.toJSONString(columns, true));
    }

    /**
     * 查询主键
     */
    @Test
    void primaryKeys() {
        List<? extends PrimaryKey> primaryKeys = query.getPrimaryKeys();
        logger.info(JSON.toJSONString(primaryKeys, true));
    }

    /**
     * 获取配置文件
     *
     * @return {@link java.util.Properties}
     */
    @Override
    public String getConfigProperties() {
        return System.getProperty("user.dir") + "/src/main/resources/properties/cachedb.properties";
    }
}
