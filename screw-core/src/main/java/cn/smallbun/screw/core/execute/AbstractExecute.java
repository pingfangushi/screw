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
package cn.smallbun.screw.core.execute;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.util.Assert;
import cn.smallbun.screw.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static cn.smallbun.screw.core.constant.DefaultConstants.DESCRIPTION;

/**
 * 抽象执行
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/4/1 22:51
 */
public abstract class AbstractExecute implements Execute {
    /**
     * LOGGER
     */
    final Logger            logger = LoggerFactory.getLogger(this.getClass());

    protected Configuration config;

    public AbstractExecute(Configuration config) {
        Assert.notNull(config, "Configuration can not be empty!");
        this.config = config;
    }

    /**
     * 获取文档名称
     *
     * @param database {@link String}
     * @return {@link String} 名称
     */
    String getDocName(String database) {
        //自定义文件名称不为空
        if (StringUtils.isNotBlank(config.getEngineConfig().getFileName())) {
            return config.getEngineConfig().getFileName();
        }
        //描述
        String description = config.getDescription();
        if (StringUtils.isBlank(description)) {
            description = DESCRIPTION;
        }
        //版本号
        String version = config.getVersion();
        if (StringUtils.isBlank(version)) {
            return database + "_" + description;
        }
        return database + "_" + description + "_" + version;
    }
}
