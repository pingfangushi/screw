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
package cn.smallbun.screw.extension.pojo.metadata.model;

import lombok.Data;

import java.io.Serializable;

/**
 * TypeModel
 *
 * @author liu·yu
 * Created by 15952866402@163.com on 2020-08-14
 */
@Data
public class TypeModel implements Serializable {

    /**
     * 数据库字段类型
     */
    private String fieldType;

    /**
     * java对象字段类型
     */
    private String classType;

    /**
     * 数据库字段名
     */
    private String fieldName;

    /**
     * java对象字段名
     */
    private String className;

    /**
     * 数据库字段注释
     */
    private String remarks;

    /**
     * get方法名
     */
    private String getName;

    /**
     * set方法名
     */
    private String setName;

}
