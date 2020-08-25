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
package cn.smallbun.screw.extension.pojo.strategy;

/**
 * 命名策略,用于转换表名和字段名
 *
 * @author liu·yu
 * Created by 15952866402@163.com on 2020-08-17
 */
public interface NameStrategy {

    /**
     * 转换表名
     *
     * @param name 输入的表名
     * @return 输出转换后的名字
     */
    String transClassName(String name);

    /**
     * 转换字段名
     *
     * @param name 输入的字段名
     * @param type {@link Class}
     * @return 输出转换后的名字
     */
    String transFieldName(String name, Class<?> type);

    /**
     * 转换set方法名
     *
     * @param name 输入的字段名
     * @param type {@link Class}
     * @return 输出的set方法名
     */
    String transSetName(String name, Class<?> type);

    /**
     * 转换get方法名
     *
     * @param name 输入的字段名
     * @param type {@link Class}
     * @return 输出的方法名
     */
    String transGetName(String name, Class<?> type);

}
