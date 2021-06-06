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

import cn.smallbun.screw.core.exception.MappingException;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

/**
 * 映射器
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on  2020/3/25
 */
public class Mapping {

    private Mapping() {
    }

    /**
     * 将ResultSet 结果转为对象
     *
     * @param <T>       领域泛型
     * @param resultSet {@link ResultSet} 对象
     * @param clazz     领域类型
     * @return 领域对象
     * @throws MappingException MappingException
     */
    public static <T> T convert(ResultSet resultSet, Class<T> clazz) throws MappingException {
        //存放列名和结果
        Map<String, Object> values = new HashMap<>(16);
        try {
            //处理 ResultSet
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            //迭代
            while (resultSet.next()) {
                //放入内存
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    values.put(columnName, resultSet.getString(columnName));
                }
            }
            //有结果
            if (values.size() != 0) {
                //获取类数据
                List<FieldMethod> fieldMethods = getFieldMethods(clazz);
                //设置属性值
                return getObject(clazz, fieldMethods, values);
            }
            return clazz.newInstance();
        } catch (Exception e) {
            throw new MappingException(e);
        }
    }

    /**
     * @param resultSet {@link ResultSet} 对象
     * @param clazz     领域类型
     * @param <T>       领域泛型
     * @return 领域对象
     * @throws MappingException MappingException
     */
    public static <T> List<T> convertList(ResultSet resultSet,
                                          Class<T> clazz) throws MappingException {
        //存放列名和结果
        List<Map<String, Object>> values = new ArrayList<>(16);
        //结果集合
        List<T> list = new ArrayList<>();
        try {
            //处理 ResultSet
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            //迭代
            while (resultSet.next()) {
                //map object
                HashMap<String, Object> value = new HashMap<>(16);
                //循环所有的列，获取列名，根据列名获取值
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    value.put(columnName, resultSet.getString(i));
                }
                //add object
                values.add(value);
            }
            //获取类数据
            List<FieldMethod> fieldMethods = getFieldMethods(clazz);
            //循环集合，根据类型反射构建对象
            for (Map<String, Object> map : values) {
                T rsp = getObject(clazz, fieldMethods, map);
                list.add(rsp);
            }
        } catch (Exception e) {
            throw new MappingException(e);
        }
        return list;
    }

    /**
     * 根据列标签获取列信息
     *
     * @param resultSet {@link ResultSet} 对象
     * @param clazz     领域类型
     * @param <T>       领域泛型
     * @return 领域对象
     * @throws MappingException MappingException
     */
    public static <T> List<T> convertListByColumnLabel(ResultSet resultSet,
                                                       Class<T> clazz) throws MappingException {
        //存放列名和结果
        List<Map<String, Object>> values = new ArrayList<>(16);
        //结果集合
        List<T> list = new ArrayList<>();
        try {
            //处理 ResultSet
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            //迭代
            while (resultSet.next()) {
                //map object
                HashMap<String, Object> value = new HashMap<>(16);
                //循环所有的列，获取列名，根据列名获取值
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    value.put(columnName, resultSet.getString(i));
                }
                //add object
                values.add(value);
            }
            //获取类数据
            List<FieldMethod> fieldMethods = getFieldMethods(clazz);
            //循环集合，根据类型反射构建对象
            for (Map<String, Object> map : values) {
                T rsp = getObject(clazz, fieldMethods, map);
                list.add(rsp);
            }
        } catch (Exception e) {
            throw new MappingException(e);
        }
        return list;
    }

    /**
     * 获取对象
     *
     * @param clazz        class
     * @param fieldMethods List<FieldMethod>
     * @param map          数据集合
     * @param <T>          领域泛型
     * @return 领域对象
     * @throws InstantiationException    InstantiationException
     * @throws IllegalAccessException    IllegalAccessException
     * @throws InvocationTargetException InvocationTargetException
     */
    private static <T> T getObject(Class<T> clazz, List<FieldMethod> fieldMethods,
                                   Map<String, Object> map) throws InstantiationException,
                                                            IllegalAccessException,
                                                            InvocationTargetException {
        T rsp = clazz.newInstance();
        //设置属性值
        for (FieldMethod filed : fieldMethods) {
            Field field = filed.getField();
            Method method = filed.getMethod();
            MappingField jsonField = field.getAnnotation(MappingField.class);
            if (!Objects.isNull(jsonField)) {
                method.invoke(rsp, map.get(jsonField.value()));
            }
        }
        return rsp;
    }

    /**
     * 根据类型获取 FieldMethod
     *
     * @param clazz {@link Class}
     * @param <T>   {@link T}
     * @return {@link List<FieldMethod>}
     * @throws IntrospectionException IntrospectionException
     * @throws NoSuchFieldException   NoSuchFieldException
     */
    private static <T> List<FieldMethod> getFieldMethods(Class<T> clazz) throws IntrospectionException,
                                                                         NoSuchFieldException {
        //结果集合
        List<FieldMethod> fieldMethods = new ArrayList<>();
        //BeanInfo
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        //循环处理值
        for (PropertyDescriptor pd : pds) {
            Method writeMethod = pd.getWriteMethod();
            if (writeMethod == null) {
                continue;
            }
            //获取字段
            Field field = clazz.getDeclaredField(pd.getName());
            //获取只写方法
            FieldMethod fieldMethod = new FieldMethod();
            fieldMethod.setField(field);
            fieldMethod.setMethod(writeMethod);
            //放入集合
            fieldMethods.add(fieldMethod);
        }
        return fieldMethods;
    }

    /**
     * 尝试获取属性
     * <p>
     * 不会抛出异常，不存在则返回null
     *
     * @param clazz {@link Class}
     * @param itemName {@link String}
     * @return {@link Field}
     */
    private static Field tryGetFieldWithoutExp(Class<?> clazz, String itemName) {
        try {
            return clazz.getDeclaredField(itemName);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取属性设置属性
     *
     * @param clazz {@link Class}
     * @param field  {@link Field}
     * @return {@link Method}
     */
    private static <T> Method tryGetSetMethod(Class<T> clazz, Field field, String methodName) {
        try {
            return clazz.getDeclaredMethod(methodName, field.getType());
        } catch (Exception e) {
            return null;
        }

    }
}
