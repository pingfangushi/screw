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

import cn.smallbun.screw.core.engine.freemark.FreemarkerTemplateEngine;
import cn.smallbun.screw.core.engine.velocity.VelocityTemplateEngine;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 模板类型
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/21 20:07
 */
public enum EngineTemplateType implements Serializable {
                                                        /**
                                                         * velocity 模板
                                                         */
                                                        velocity("/template/velocity/",
                                                                 VelocityTemplateEngine.class,
                                                                 ".vm"),
                                                        /**
                                                         * freeMarker 模板
                                                         */
                                                        freemarker("/template/freemarker/",
                                                                   FreemarkerTemplateEngine.class,
                                                                   ".ftl");

    /**
     * 模板目录
     */
    @Getter
    @Setter
    private String                          templateDir;
    /**
     * 模板驱动实现类类型
     */
    @Getter
    @Setter
    private Class<? extends TemplateEngine> implClass;
    /**
     * 后缀
     */
    @Getter
    @Setter
    private String                          suffix;

    /**
     * 构造
     *
     * @param freemarker {@link String}
     * @param template   {@link Class}
     */
    EngineTemplateType(String freemarker, Class<? extends TemplateEngine> template, String suffix) {
        this.templateDir = freemarker;
        this.implClass = template;
        this.suffix = suffix;
    }
}
