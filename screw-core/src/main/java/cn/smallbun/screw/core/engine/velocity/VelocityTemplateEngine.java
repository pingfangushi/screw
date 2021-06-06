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
package cn.smallbun.screw.core.engine.velocity;

import cn.smallbun.screw.core.engine.AbstractTemplateEngine;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.exception.ProduceException;
import cn.smallbun.screw.core.metadata.model.DataModel;
import cn.smallbun.screw.core.util.Assert;
import cn.smallbun.screw.core.util.ExceptionUtils;
import cn.smallbun.screw.core.util.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.log.NullLogChute;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static cn.smallbun.screw.core.constant.DefaultConstants.DEFAULT_ENCODING;
import static cn.smallbun.screw.core.engine.EngineTemplateType.velocity;

/**
 * velocity template
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/17 21:40
 */
public class VelocityTemplateEngine extends AbstractTemplateEngine {
    /**
     * DATA
     */
    private static final String DATA = "_data";

    /**
     * 构造函数
     *
     * @param templateConfig {@link EngineConfig }
     */
    public VelocityTemplateEngine(EngineConfig templateConfig) {
        super(templateConfig);
    }

    /**
     * VelocityEngine
     */
    private static VelocityEngine velocityEngine;

    {
        // 初始化模板引擎
        velocityEngine = new VelocityEngine();
        // 如果存在自定义模板
        if (StringUtils.isNotBlank(getEngineConfig().getCustomTemplate())) {
            velocityEngine.setProperty("string.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
        } else {
            velocityEngine.setProperty("file.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        }
        velocityEngine.setProperty("runtime.log.logsystem.class", NullLogChute.class.getName());
        velocityEngine.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, "");
        velocityEngine.setProperty(Velocity.ENCODING_DEFAULT, DEFAULT_ENCODING);
        velocityEngine.setProperty(Velocity.INPUT_ENCODING, DEFAULT_ENCODING);
        velocityEngine.setProperty("file.resource.loader.unicode", "true");
    }

    /**
     * 生成文档
     *
     * @param info {@link DataModel}
     * @throws ProduceException ProduceException
     */
    @Override
    public void produce(DataModel info, String docName) throws ProduceException {
        Assert.notNull(info, "DataModel can not be empty!");
        Template template;
        try {
            // get template path
            String path = getEngineConfig().getCustomTemplate();
            //如果自定义了模板
            if (StringUtils.isNotBlank(path)) {
                template = velocityEngine.getTemplate(path, DEFAULT_ENCODING);
            }
            //没有自定义模板，使用核心包自带
            else {
                template = velocityEngine
                    .getTemplate(velocity.getTemplateDir()
                                 + getEngineConfig().getFileType().getTemplateNamePrefix()
                                 + velocity.getSuffix(),
                        DEFAULT_ENCODING);
            }
            // output
            try (FileOutputStream outStream = new FileOutputStream(getFile(docName));
                    OutputStreamWriter writer = new OutputStreamWriter(outStream, DEFAULT_ENCODING);
                    BufferedWriter sw = new BufferedWriter(writer)) {
                //put data
                VelocityContext context = new VelocityContext();
                context.put(DATA, info);
                //generate
                template.merge(context, sw);
                // open the output directory
                openOutputDir();
            }
        } catch (IOException e) {
            throw ExceptionUtils.mpe(e);
        }
    }
}
