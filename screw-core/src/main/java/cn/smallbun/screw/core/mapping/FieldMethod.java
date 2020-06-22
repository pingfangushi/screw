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
package cn.smallbun.screw.core.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * FieldMethod
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on  2020/3/25
 */
public class FieldMethod {

    /**
     * 属性
     */
    private Field  field;

    /**
     * 方法
     */
    private Method method;

    /**
     * Getter method for property <tt>field</tt>.
     *
     * @return property value of field
     */
    public Field getField() {
        return field;
    }

    /**
     * Setter method for property <tt>field</tt>.
     *
     * @param field value to be assigned to property field
     */
    public void setField(Field field) {
        this.field = field;
    }

    /**
     * Getter method for property <tt>method</tt>.
     *
     * @return property value of method
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Setter method for property <tt>method</tt>.
     *
     * @param method value to be assigned to property method
     */
    public void setMethod(Method method) {
        this.method = method;
    }

}
