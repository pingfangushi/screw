/*
 * screw-extension - 简洁好用的数据库表结构文档生成工具
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
package cn.smallbun.screw.extension.pojo.dialect;

import cn.smallbun.screw.core.exception.ScrewException;
import cn.smallbun.screw.core.query.DatabaseQuery;
import cn.smallbun.screw.core.query.DatabaseType;
import cn.smallbun.screw.core.util.ExceptionUtils;
import cn.smallbun.screw.core.util.JdbcUtils;
import cn.smallbun.screw.extension.pojo.dialect.mysql.MysqlTypeDialect;
import lombok.Getter;

import javax.sql.DataSource;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 类型转换工厂
 *
 * @author liu·yu
 * Created by 15952866402@163.com on 2020-08-17
 */
public class TypeDialectFactory {

    /**
     * DataSource
     */
    @Getter
    private final DataSource                                      dataSource;

    private final Map<DatabaseType, Class<? extends TypeDialect>> dialectMap;

    {
        dialectMap = new HashMap<>();
        dialectMap.put(DatabaseType.MYSQL, MysqlTypeDialect.class);
    }

    public TypeDialectFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 获取配置的数据库类型实例
     *
     * @return {@link DatabaseQuery} 数据库查询对象
     */
    public TypeDialect newInstance() {
        try {
            //获取数据库URL 用于判断数据库类型
            String url = this.getDataSource().getConnection().getMetaData().getURL();
            //获取实现类
            Class<? extends TypeDialect> query = dialectMap.get(JdbcUtils.getDbType(url));
            //判断是否已经实现实现
            if (query == null)
                throw new ScrewException("this database has not support url:" + url);
            //获取有参构造
            Constructor<? extends TypeDialect> constructor = query.getConstructor();
            //实例化
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
                | InvocationTargetException | SQLException e) {
            throw ExceptionUtils.mpe(e);
        }
    }

}
