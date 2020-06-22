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
package cn.smallbun.screw.core.engine;

import cn.smallbun.screw.core.exception.ProduceException;
import cn.smallbun.screw.core.util.Assert;
import lombok.Getter;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 生成构造工厂
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/21 21:20
 */
public class EngineFactory implements Serializable {
    /**
     * EngineConfig
     */
    @Getter
    private EngineConfig engineConfig;

    public EngineFactory(EngineConfig configuration) {
        Assert.notNull(configuration, "EngineConfig can not be empty!");
        this.engineConfig = configuration;
    }

    private EngineFactory() {
    }

    /**
     * 获取配置的数据库类型实例
     *
     * @return {@link TemplateEngine} 数据库查询对象
     */
    public TemplateEngine newInstance() {
        try {
            //获取实现类
            Class<? extends TemplateEngine> query = this.engineConfig.getProduceType()
                .getImplClass();
            //获取有参构造
            Constructor<? extends TemplateEngine> constructor = query
                .getConstructor(EngineConfig.class);
            //实例化
            return (TemplateEngine) constructor.newInstance(engineConfig);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
                | InvocationTargetException e) {
            throw new ProduceException(e);
        }
    }
}
