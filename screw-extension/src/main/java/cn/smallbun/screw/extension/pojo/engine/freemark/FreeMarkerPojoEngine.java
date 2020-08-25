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
package cn.smallbun.screw.extension.pojo.engine.freemark;

import cn.smallbun.screw.core.exception.ProduceException;
import cn.smallbun.screw.core.exception.ScrewException;
import cn.smallbun.screw.extension.pojo.engine.PojoEngine;
import cn.smallbun.screw.extension.pojo.metadata.model.PojoModel;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;

import static cn.smallbun.screw.core.constant.DefaultConstants.DEFAULT_ENCODING;

/**
 * freemark pojo 引擎
 *
 * @author liu·yu
 * Created by 15952866402@163.com on 2020-08-14
 */
public class FreeMarkerPojoEngine implements PojoEngine {

    private final Configuration configuration = new Configuration(
        Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);

    {
        configuration
            .setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/template/freemarker/"));
    }

    @Override
    public void produce(PojoModel info, String docName) throws ProduceException {

        try {
            Template template = configuration.getTemplate("pojo_java.ftl");
            // create file
            File file = new File(docName);
            if (file.exists()) {
                throw new ScrewException("file need to generate has been exist! path:" + docName);
            }
            // writer freemarker
            try (Writer out = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), DEFAULT_ENCODING))) {
                // process
                template.process(info, out);
                // open the output directory
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
