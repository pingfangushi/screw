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
package cn.smallbun.screw.extension.pojo.dialect.mysql;

import cn.smallbun.screw.extension.pojo.dialect.TypeDialect;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * mysql 类型字典
 *
 * @author liu·yu
 * Created by 15952866402@163.com on 2020-08-17
 */
public class MysqlTypeDialect implements TypeDialect {

    private final Map<String, Class<?>> map = new HashMap<>();

    public MysqlTypeDialect() {
        map.put("varchar", String.class);
        map.put("char", String.class);
        map.put("blob", Byte[].class);
        map.put("text", String.class);
        map.put("int", Integer.class);
        map.put("tinyint", Integer.class);
        map.put("smallint", Integer.class);
        map.put("mediumint", Integer.class);
        map.put("bit", Boolean.class);
        map.put("bigint", Long.class);
        map.put("float", Float.class);
        map.put("double", Double.class);
        map.put("decimal", BigDecimal.class);
        map.put("date", LocalDate.class);
        map.put("time", LocalTime.class);
        map.put("datetime", LocalDateTime.class);
        map.put("timestamp", LocalDateTime.class);
    }

    @Override
    public Class<?> getClassTypeByFieldType(String type) {
        Class<?> clazz = getTypeByMap(map, type);
        if (clazz == null) {
            clazz = Object.class;
        }
        return clazz;
    }

}
