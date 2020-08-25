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
import java.util.List;
import java.util.Set;

/**
 * PojoModel 定义一个pojo需要的一些属性
 *
 * @author liu·yu
 * Created by 15952866402@163.com on 2020-08-14
 */
@Data
public class PojoModel implements Serializable {

    /**
     * 包名
     */
    private String          packageName;

    /**
     * 类名
     */
    private String          className;

    /**
     * 数据库表名
     */
    private String          tableName;

    /**
     * 数据库注释
     */
    private String          remarks;

    /**
     * 是否使用lombok
     */
    private boolean         useLombok;

    /**
     * 需要import的类
     */
    private Set<String>     importList;

    /**
     * 对象的字段属性
     */
    private List<TypeModel> fieldList;

}
