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
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Properties;

import static cn.smallbun.screw.core.common.Constants.fileOutputDir;

/**
 * SqlServer 文档生成测试
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/30 18:33
 */
public class Db2DocumentationBuilderTest extends AbstractDocumentationExecute {

    /**
     * 构建
     */
    @Test
    void build() throws IOException {
        //数据源
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(getDriver());
        hikariConfig.setJdbcUrl(getUrl());
        hikariConfig.setUsername(getUserName());
        hikariConfig.setPassword(getPassword());
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaximumPoolSize(5);
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        //生成配置
        EngineConfig engineConfig = EngineConfig.builder()
            //生成文件路径
            .fileOutputDir(fileOutputDir)
            //文件类型
            .fileType(EngineFileType.MD)
            //打开文件夹
            .openOutputDir(true)
            //生成模板实现
            .produceType(EngineTemplateType.velocity).build();
        //配置
        Configuration config = Configuration.builder()
            //版本
            .version("1.0.0")
            //描述
            .description("数据库设计文档生成")
            //数据源
            .dataSource(dataSource)
            //生成配置
            .engineConfig(engineConfig).build();
        execute(config);
    }

    /**
     * 获取配置文件
     * @return {@link Properties}
     */
    @Override
    public String getConfigProperties() {
        return System.getProperty("user.dir") + "/src/main/resources/properties/db2.properties";
    }
}
