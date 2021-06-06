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

import java.util.Map;

/**
 * 根据获取的数据库字段类型，决定使用java对象类型
 *
 * @author liu·yu
 * Created by 15952866402@163.com on 2020-08-17
 */
public interface TypeDialect {
    /**
     * 通过字段类型获取类类型
     * @param type {@link String}
     * @return {@link Class}
     */
    Class<?> getClassTypeByFieldType(String type);

    /**
     * 根据提供的map查询对应的java类型
     *
     * @param map  提供的map，不可为null
     * @param type 提供的类型
     * @return 查找类型，可能null
     */
    default Class<?> getTypeByMap(Map<String, Class<?>> map, String type) {
        if (type == null || map == null || map.size() == 0) {
            return null;
        }
        type = type.toLowerCase();

        if (type.startsWith("date")) {
            return map.get("date");
        }
        if (type.startsWith("mediumint")) {
            return map.get("mediumint");
        }
        if (type.startsWith("double")) {
            return map.get("double");
        }
        if (type.startsWith("varchar")) {
            return map.get("varchar");
        }
        if (type.startsWith("tinyint")) {
            return map.get("tinyint");
        }
        if (type.startsWith("bit")) {
            return map.get("bit");
        }
        if (type.startsWith("float")) {
            return map.get("float");
        }
        if (type.startsWith("int")) {
            return map.get("int");
        }
        if (type.startsWith("smallint")) {
            return map.get("smallint");
        }
        if (type.startsWith("datetime")) {
            return map.get("datetime");
        }
        if (type.startsWith("blob")) {
            return map.get("blob");
        }
        if (type.startsWith("char")) {
            return map.get("char");
        }
        if (type.startsWith("text")) {
            return map.get("text");
        }
        if (type.startsWith("time")) {
            return map.get("time");
        }
        if (type.startsWith("decimal")) {
            return map.get("decimal");
        }
        if (type.startsWith("bigint")) {
            return map.get("bigint");
        }
        if (type.startsWith("timestamp")) {
            return map.get("timestamp");
        }
        return null;
    }

}
