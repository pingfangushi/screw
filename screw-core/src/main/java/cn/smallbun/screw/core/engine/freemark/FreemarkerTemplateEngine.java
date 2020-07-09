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
package cn.smallbun.screw.core.engine.freemark;

import cn.smallbun.screw.core.engine.AbstractTemplateEngine;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.exception.ProduceException;
import cn.smallbun.screw.core.metadata.model.DataModel;
import cn.smallbun.screw.core.util.Assert;
import cn.smallbun.screw.core.util.ExceptionUtils;
import cn.smallbun.screw.core.util.FileUtils;
import cn.smallbun.screw.core.util.StringUtils;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Locale;
import java.util.Objects;

import static cn.smallbun.screw.core.constant.DefaultConstants.DEFAULT_ENCODING;
import static cn.smallbun.screw.core.constant.DefaultConstants.DEFAULT_LOCALE;
import static cn.smallbun.screw.core.engine.EngineTemplateType.freemarker;
import static cn.smallbun.screw.core.util.FileUtils.getFileByPath;
import static cn.smallbun.screw.core.util.FileUtils.isFileExists;

/**
 * freemarker template produce
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/17 21:40
 */
public class FreemarkerTemplateEngine extends AbstractTemplateEngine {
    /**
     * freemarker 配置实例化
     */
    private final Configuration configuration = new Configuration(
        Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);

    {
        try {
            String path = getEngineConfig().getCustomTemplate();
            //自定义模板
            if (StringUtils.isNotBlank(path) && FileUtils.isFileExists(path)) {
                //获取父目录
                String parent = Objects.requireNonNull(getFileByPath(path)).getParent();
                //设置模板加载路径
                configuration.setDirectoryForTemplateLoading(new File(parent));
            }
            //加载自带模板
            else {
                //模板存放路径
                configuration.setTemplateLoader(
                    new ClassTemplateLoader(this.getClass(), freemarker.getTemplateDir()));
            }
            //编码
            configuration.setDefaultEncoding(DEFAULT_ENCODING);
            //国际化
            configuration.setLocale(new Locale(DEFAULT_LOCALE));
        } catch (Exception e) {
            throw ExceptionUtils.mpe(e);
        }
    }

    public FreemarkerTemplateEngine(EngineConfig templateConfig) {
        super(templateConfig);
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
        String path = getEngineConfig().getCustomTemplate();
        try {
            Template template;
            // freemarker template
            // 如果自定义路径不为空文件也存在
            if (StringUtils.isNotBlank(path) && isFileExists(path)) {
                // 文件名称
                String fileName = new File(path).getName();
                template = configuration.getTemplate(fileName);
            }
            //获取系统默认的模板
            else {
                template = configuration
                    .getTemplate(getEngineConfig().getFileType().getTemplateNamePrefix()
                                 + freemarker.getSuffix());
            }
            // create file
            File file = getFile(docName);
            // writer freemarker
            try (Writer out = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), DEFAULT_ENCODING))) {
                // process
                template.process(info, out);
                // open the output directory
                openOutputDir();
            }
        } catch (IOException | TemplateException e) {
            throw ExceptionUtils.mpe(e);
        }
    }
}
