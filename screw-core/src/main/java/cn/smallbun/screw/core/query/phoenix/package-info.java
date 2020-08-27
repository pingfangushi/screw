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
/**
 * Database query
 *
 * @author xielong.wang
 * Created by siaron.wang@gmail.com
 */
package cn.smallbun.screw.core.query.phoenix;
//phoenix 默认无法添加注释的(不解析 COMMENT ON COLUMN 语句). 但是可以再catalog表中remark字段上手动增加注释.符合jdbc的规范.导出时可以有注释的.