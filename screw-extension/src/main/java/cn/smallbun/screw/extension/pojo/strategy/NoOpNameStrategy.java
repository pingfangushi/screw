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
 * 没有任何操作的命名策略
 *
 * @author liu·yu
 * Created by 15952866402@163.com on 2020-08-17
 */
public class NoOpNameStrategy implements NameStrategy {

    @Override
    public String transClassName(String name) {
        return name;
    }

    @Override
    public String transFieldName(String name, Class<?> type) {
        return name;
    }

    @Override
    public String transSetName(String name, Class<?> type) {
        return "set" + name;
    }

    @Override
    public String transGetName(String name, Class<?> type) {
        return "get" + name;
    }

}
