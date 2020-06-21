/*
 * screw-core - 简洁好用的数据库文档生成器
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

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.metadata.Column;
import cn.smallbun.screw.core.metadata.PrimaryKey;
import cn.smallbun.screw.core.metadata.Table;
import cn.smallbun.screw.core.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AbstractBuilder
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/22 21:09
 */
public abstract class AbstractProcess implements Process {
    /**
     * LOGGER
     */
    final Logger                                logger             = LoggerFactory
        .getLogger(this.getClass());
    /**
     * 表信息缓存
     */
    volatile Map<String, List<? extends Table>> tablesCaching      = new ConcurrentHashMap<>();
    /**
     * 列信息缓存
     */
    volatile Map<String, List<Column>>          columnsCaching     = new ConcurrentHashMap<>();
    /**
     * 主键信息缓存
     */
    volatile Map<String, List<PrimaryKey>>      primaryKeysCaching = new ConcurrentHashMap<>();

    /**
     * Configuration
     */
    protected Configuration                     config;

    private AbstractProcess() {
    }

    /**
     * 构造方法
     *
     * @param configuration     {@link Configuration}
     */
    AbstractProcess(Configuration configuration) {
        Assert.notNull(configuration, "Configuration can not be empty!");
        this.config = configuration;
    }
}
