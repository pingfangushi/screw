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
package cn.smallbun.screw.extension.pojo.execute;

import cn.smallbun.screw.core.exception.ScrewException;
import cn.smallbun.screw.core.execute.Execute;
import cn.smallbun.screw.core.util.ExceptionUtils;
import cn.smallbun.screw.core.util.StringUtils;
import cn.smallbun.screw.extension.pojo.PojoConfiguration;
import cn.smallbun.screw.extension.pojo.engine.PojoEngine;
import cn.smallbun.screw.extension.pojo.engine.freemark.FreeMarkerPojoEngine;
import cn.smallbun.screw.extension.pojo.metadata.model.PojoModel;
import cn.smallbun.screw.extension.pojo.process.PojoModelProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * java bean生成
 *
 * @author liu·yu
 * Created by 15952866402@163.com on 2020-08-14
 */
public class PojoExecute implements Execute {

    /**
     * LOGGER
     */
    private final Logger            logger = LoggerFactory.getLogger(this.getClass());

    private final PojoConfiguration configuration;

    public PojoExecute(PojoConfiguration config) {
        String validate = validate(config);
        if (StringUtils.isNotBlank(validate)) {
            throw new ScrewException(validate);
        }
        this.configuration = config;
    }

    @Override
    public void execute() {
        try {
            long start = System.currentTimeMillis();
            //获取数据
            List<PojoModel> process = new PojoModelProcess(configuration).getPojoModel();
            //新建模板引擎
            PojoEngine pojoEngine = new FreeMarkerPojoEngine();
            //确认路径
            String path = configuration.getPath();
            if (!path.endsWith("/")) {
                path = path + "/";
            }
            File pathFile = new File(path);
            if (!pathFile.exists()) {
                boolean mkdir = pathFile.mkdirs();
                if (!mkdir) {
                    throw new ScrewException("create directory failed");
                }
            }
            if (!pathFile.isDirectory()) {
                throw new ScrewException("path is not a directory");
            }
            //逐个产生文档
            for (PojoModel pojoModel : process) {
                pojoEngine.produce(pojoModel, path + pojoModel.getClassName() + ".java");
            }
            logger.debug("pojo generation complete time consuming:{}ms",
                System.currentTimeMillis() - start);
        } catch (Exception e) {
            throw ExceptionUtils.mpe(e);
        }
    }

    private String validate(PojoConfiguration config) {
        StringBuilder error = new StringBuilder();
        String separator = System.lineSeparator();
        if (config == null) {
            error.append(separator);
            error.append("config can't be null!");
            return error.toString();
        }
        if (StringUtils.isBlank(config.getPackageName())) {
            error.append(separator);
            error.append("package can't be null!");
        }
        if (StringUtils.isBlank(config.getPath())) {
            error.append(separator);
            error.append("path can't be null!");
        }
        if (config.getDataSource() == null) {
            error.append(separator);
            error.append("datasource can't be null!");
        }
        if (config.getNameStrategy() == null) {
            error.append(separator);
            error.append("name strategy can't be null!");
        }
        return error.toString();
    }

}
