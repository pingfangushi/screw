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
package cn.smallbun.screw.core.produce;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.common.Properties;
import cn.smallbun.screw.core.execute.DocumentationExecute;

/**
 * 抽象的数据库文档生成
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/30 18:28
 */
public abstract class AbstractDocumentationExecute implements Properties {

    /**
     * 构建
     *
     * @param configuration {@link Configuration}
     */
    void execute(Configuration configuration) {
        //生成
        new DocumentationExecute(configuration).execute();
    }

}
