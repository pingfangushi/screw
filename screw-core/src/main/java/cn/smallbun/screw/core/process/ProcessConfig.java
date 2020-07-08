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
package cn.smallbun.screw.core.process;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 数据处理
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/4/1 22:22
 */
@Data
@Builder
public class ProcessConfig implements Serializable {
    /**
     * 忽略表名
     */
    private List<String> ignoreTableName;
    /**
     * 忽略表前缀
     */
    private List<String> ignoreTablePrefix;
    /**
     * 忽略表后缀
     */
    private List<String> ignoreTableSuffix;
    /**
     * 指定生成表名
     * @see "1.0.3"
     */
    private List<String> designatedTableName;
    /**
     * 指定生成表前缀
     * @see "1.0.3"
     */
    private List<String> designatedTablePrefix;
    /**
     * 指定生成表后缀
     * @see "1.0.3"
     */
    private List<String> designatedTableSuffix;
}
