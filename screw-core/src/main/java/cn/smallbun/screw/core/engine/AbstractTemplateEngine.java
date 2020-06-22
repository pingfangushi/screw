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

import cn.smallbun.screw.core.util.Assert;
import cn.smallbun.screw.core.util.ExceptionUtils;
import cn.smallbun.screw.core.util.StringUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static cn.smallbun.screw.core.constant.DefaultConstants.MAC;
import static cn.smallbun.screw.core.constant.DefaultConstants.WINDOWS;

/**
 * AbstractProduce
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/17 21:47
 */
@Data
public abstract class AbstractTemplateEngine implements TemplateEngine {
    /**
     * 日志
     */
    final Logger         logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 模板配置
     */
    private EngineConfig engineConfig;

    private AbstractTemplateEngine() {
    }

    public AbstractTemplateEngine(EngineConfig engineConfig) {
        Assert.notNull(engineConfig, "EngineConfig can not be empty!");
        this.engineConfig = engineConfig;
    }

    /**
     * 获取文件，文件名格式为，数据库名_版本号.文件类型
     *
     * @param docName 文档名称
     * @return {@link String}
     */
    protected File getFile(String docName) {
        File file;
        //如果没有填写输出路径，默认当前项目路径下的doc目录
        if (StringUtils.isBlank(getEngineConfig().getFileOutputDir())) {
            String dir = System.getProperty("user.dir");
            file = new File(dir + "/doc");
        } else {
            file = new File(getEngineConfig().getFileOutputDir());
        }
        //不存在创建
        if (!file.exists()) {
            //创建文件夹
            boolean mkdir = file.mkdirs();
        }
        //文件后缀
        String suffix = getEngineConfig().getFileType().getFileSuffix();
        file = new File(file, docName + suffix);
        //设置文件产生位置
        getEngineConfig().setFileOutputDir(file.getParent());
        return file;
    }

    /**
     * 打开文档生成的输出目录
     */
    protected void openOutputDir() {
        //是否打开，如果是就打开输出路径
        if (getEngineConfig().isOpenOutputDir()
            && StringUtils.isNotBlank(getEngineConfig().getFileOutputDir())) {
            try {
                //获取系统信息
                String osName = System.getProperty("os.name");
                if (osName != null) {
                    if (osName.contains(MAC)) {
                        Runtime.getRuntime().exec("open " + getEngineConfig().getFileOutputDir());
                    } else if (osName.contains(WINDOWS)) {
                        Runtime.getRuntime()
                            .exec("cmd /c start " + getEngineConfig().getFileOutputDir());
                    }
                }
            } catch (IOException e) {
                throw ExceptionUtils.mpe(e);
            }
        }
    }
}
