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

import java.util.Collection;
import java.util.Map;

/**
 * 断言
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/30 22:18
 */
public class Assert {

    /**
     * 断言 boolean 为 true
     * <p>为 false 则抛出异常</p>
     *
     * @param expression {@link Boolean} boolean 值
     * @param message    {@link String}消息
     * @param params     {@link String}    参数
     */
    public static void isTrue(boolean expression, String message, Object... params) {
        if (!expression) {
            throw ExceptionUtils.mpe(message, params);
        }
    }

    /**
     * 断言 boolean 为 false
     * <p>为 true 则抛出异常</p>
     *
     * @param expression {@link Boolean}  boolean 值
     * @param message    {@link String}   消息
     * @param params {@link Object}
     */
    public static void isFalse(boolean expression, String message, Object... params) {
        isTrue(!expression, message, params);
    }

    /**
     * 断言 object 为 null
     * <p>不为 null 则抛异常</p>
     *
     * @param object  {@link String}  对象
     * @param message {@link String}消息
     * @param params  {@link String}    参数
     */
    public static void isNull(Object object, String message, Object... params) {
        isTrue(object == null, message, params);
    }

    /**
     * 断言 object 不为 null
     * <p>为 null 则抛异常</p>
     *
     * @param object  {@link String}  对象
     * @param message {@link String}消息
     * @param params  {@link String}    参数
     */
    public static void notNull(Object object, String message, Object... params) {
        isTrue(object != null, message, params);
    }

    /**
     * 断言 value 不为 empty
     * <p>为 empty 则抛异常</p>
     *
     * @param value   {@link String} 字符串
     * @param message {@link String}消息
     * @param params  {@link String}    参数
     */
    public static void notEmpty(String value, String message, Object... params) {
        isTrue(StringUtils.isNotBlank(value), message, params);
    }

    /**
     * 断言 collection 不为 empty
     * <p>为 empty 则抛异常</p>
     *
     * @param collection {@link Collection} 集合
     * @param message    {@link String}消息
     * @param params     {@link String}    参数
     */
    public static void notEmpty(Collection<?> collection, String message, Object... params) {
        isTrue(CollectionUtils.isNotEmpty(collection), message, params);
    }

    /**
     * 断言 map 不为 empty
     * <p>为 empty 则抛异常</p>
     *
     * @param map     {@link Map} 集合
     * @param message {@link String}消息
     * @param params  {@link String}    参数
     */
    public static void notEmpty(Map<?, ?> map, String message, Object... params) {
        isTrue(CollectionUtils.isNotEmpty(map), message, params);
    }

    /**
     * 断言 数组 不为 empty
     * <p>为 empty 则抛异常</p>
     *
     * @param array   {@link Object}  数组
     * @param message {@link String}消息
     * @param params  {@link String}    参数
     */
    public static void notEmpty(Object[] array, String message, Object... params) {
        isTrue(ArrayUtils.isNotEmpty(array), message, params);
    }
}
