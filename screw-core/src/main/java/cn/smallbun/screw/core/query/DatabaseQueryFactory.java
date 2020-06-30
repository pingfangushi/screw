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
package cn.smallbun.screw.core.query;

import cn.smallbun.screw.core.util.ExceptionUtils;
import cn.smallbun.screw.core.util.JdbcUtils;
import lombok.Getter;

import javax.sql.DataSource;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

/**
 * 数据库查询工厂
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/18 14:15
 */
public class DatabaseQueryFactory implements Serializable {
    /**
     * DataSource
     */
    @Getter
    private DataSource dataSource;

    /**
     * 构造函数私有化
     * 禁止通过new方式实例化对象
     */
    private DatabaseQueryFactory() {
    }

    /**
     * 构造函数
     *
     * @param source {@link DataSource}
     */
    public DatabaseQueryFactory(DataSource source) {
        dataSource = source;
    }

    /**
     * 获取配置的数据库类型实例
     *
     * @return {@link DatabaseQuery} 数据库查询对象
     */
    public DatabaseQuery newInstance() {
        try {
            //获取数据库URL 用于判断数据库类型
            String url = this.getDataSource().getConnection().getMetaData().getURL();
            //获取实现类
            Class<? extends DatabaseQuery> query = JdbcUtils.getDbType(url).getImplClass();
            //获取有参构造
            Constructor<? extends DatabaseQuery> constructor = query
                .getConstructor(DataSource.class);
            //实例化
            return constructor.newInstance(dataSource);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
                | InvocationTargetException | SQLException e) {
            throw ExceptionUtils.mpe(e);
        }
    }
}
