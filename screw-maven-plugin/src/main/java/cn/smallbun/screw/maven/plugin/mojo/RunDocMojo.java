/*
 * screw-maven-plugin - 简洁好用的数据库表结构文档生成工具
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
package cn.smallbun.screw.maven.plugin.mojo;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import cn.smallbun.screw.core.query.DatabaseType;
import cn.smallbun.screw.core.util.FileUtils;
import cn.smallbun.screw.core.util.JdbcUtils;
import cn.smallbun.screw.core.util.StringUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

import static cn.smallbun.screw.core.constant.DefaultConstants.*;
import static cn.smallbun.screw.core.util.FileUtils.getRealFilePath;

/**
 * Database document generation mojo
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on  2020/4/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Mojo(name = "run", threadSafe = true, defaultPhase = LifecyclePhase.COMPILE)
public class RunDocMojo extends AbstractMojo {

    //====================基本配置====================//
    /**
     * 组织
     */
    @Parameter(property = "organization")
    private String             organization;
    /**
     * url
     */
    @Parameter(property = "organizationUrl")
    private String             organizationUrl;
    /**
     * 标题
     */
    @Parameter(property = "title")
    private String             title;
    /**
     * 版本
     */
    @Parameter(property = "version")
    private String             version;
    /**
     * 描述
     */
    @Parameter(property = "description")
    private String             description;

    //====================连接配置====================//
    /**
     * 用户名
     */
    @Parameter
    private volatile String    username;
    /**
     * 密码
     */
    @Parameter
    private volatile String    password;

    /**
     * 驱动类名称
     */
    @Parameter
    private String             driverClassName;
    /**
     * JDBC URL
     */
    @Parameter
    private String             jdbcUrl;

    //====================数据处理配置====================//
    /**
     * 忽略表名
     */
    @Parameter
    private List<String>       ignoreTableName;
    /**
     * 忽略表前缀
     */
    @Parameter
    private List<String>       ignoreTablePrefix;
    /**
     * 忽略表后缀
     */
    @Parameter
    private List<String>       ignoreTableSuffix;
    /**
     * 指定生成表名
     * @see 1.0.3
     */
    @Parameter
    private List<String>       designatedTableName;
    /**
     * 指定生成表前缀
     * @see 1.0.3
     */
    @Parameter
    private List<String>       designatedTablePrefix;
    /**
     * 指定生成表后缀
     * @see 1.0.3
     */
    @Parameter
    private List<String>       designatedTableSuffix;

    //====================生成引擎配置====================//
    /**
     * 是否打开输出目录
     */
    @Parameter(defaultValue = "true")
    private boolean            openOutputDir;
    /**
     * 文件产生位置
     */
    @Parameter
    private String             fileOutputDir;
    /**
     * 生成文件类型
     */
    @Parameter(defaultValue = "HTML")
    private EngineFileType     fileType;
    /**
     * 生成实现
     */
    @Parameter(defaultValue = "freemarker")
    private EngineTemplateType produceType;
    /**
     * 自定义模板，模板需要和文件类型和使用模板的语法进行编写和处理，否则将会生成错误
     */
    @Parameter
    private String             template;

    /**
     * 文件名称
     */
    @Parameter
    private String             fileName;

    /**
     * 文档生成
     */
    @Override
    public void execute() throws MojoFailureException {
        getLog().info(LOGGER_BEGINS);
        //参数为空
        long start = System.currentTimeMillis();
        validator();
        //总配置
        Configuration config = Configuration.builder()
            //组织
            .organization(getOrganization())
            //url
            .organizationUrl(getOrganizationUrl())
            //标题
            .title(getTitle())
            //版本
            .version(getVersion())
            //描述
            .description(getDescription())
            //数据源
            .dataSource(getDataSource())
            //引擎模板配置
            .engineConfig(getEngineConfig())
            //数据处理配置
            .produceConfig(getProcessConfig()).build();
        //生成文档
        new DocumentationExecute(config).execute();
        getLog().info(String.format(LOGGER_COMPLETE, (System.currentTimeMillis() - start) / 1000));
    }

    /**
     * 引擎模板配置
     *
     * @return {@link EngineConfig}
     */
    private EngineConfig getEngineConfig() {
        return EngineConfig.builder()
            //是否打开目录
            .openOutputDir(isOpenOutputDir())
            //生成文件路径
            .fileOutputDir(getFileOutputDir())
            //文件类型
            .fileType(getFileType())
            //生成模板实现
            .produceType(getProduceType())
            //自定义模板位置
            .customTemplate(getTemplate())
            //文件名称
            .fileName(getFileName()).build();
    }

    /**
     * 数据处理配置
     *
     * @return {@link ProcessConfig}
     */
    private ProcessConfig getProcessConfig() {
        return ProcessConfig.builder()
            //忽略表名
            .ignoreTableName(getIgnoreTableName())
            //忽略表前缀
            .ignoreTablePrefix(getIgnoreTablePrefix())
            //忽略表后缀
            .ignoreTableSuffix(getIgnoreTableSuffix())
            //指定生成表名
            .designatedTableName(getDesignatedTableName())
            //指定生成表前缀
            .designatedTablePrefix(getDesignatedTablePrefix())
            //指定生成表后缀
            .designatedTableSuffix(getDesignatedTableSuffix()).build();
    }

    /**
     * 获取数据源
     *
     * @return {@link DataSource}
     */
    private DataSource getDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setPoolName(NAME);
        //驱动名称
        hikariConfig.setDriverClassName(getDriverClassName().trim());
        //url
        hikariConfig.setJdbcUrl(getJdbcUrl());
        //用户名
        hikariConfig.setUsername(getUsername());
        //密码
        hikariConfig.setPassword(getPassword());
        //mysql oracle
        if (JdbcUtils.getDbType(getJdbcUrl()).equals(DatabaseType.MYSQL)
            || JdbcUtils.getDbType(getJdbcUrl()).equals(DatabaseType.MARIADB)) {
            hikariConfig.addDataSourceProperty(USE_INFORMATION_SCHEMA, "true");
        }
        //phoenix
        if (JdbcUtils.getDbType(getJdbcUrl()).equals(DatabaseType.PHOENIX)) {
            hikariConfig.addDataSourceProperty(PHOENIX_SYS_NAMESPACE_MAPPING, true);
            hikariConfig.addDataSourceProperty(PHOENIX_NAMESPACE_MAPPING, true);
        }
        //oracle
        if (JdbcUtils.getDbType(getJdbcUrl()).equals(DatabaseType.ORACLE)) {
            hikariConfig.addDataSourceProperty(ORACLE_REMARKS, "true");
        }
        return new HikariDataSource(hikariConfig);
    }

    /**
     * 参数验证
     *
     * @throws MojoFailureException MojoFailureException
     */
    private void validator() throws MojoFailureException {
        //基础配置验证
        if (StringUtils.isBlank(getUsername())) {
            throw new MojoFailureException("请配置数据库用户名");
        }
        if (StringUtils.isBlank(getDriverClassName())) {
            throw new MojoFailureException("请配置数据库驱动名称");
        }
        if (StringUtils.isBlank(getJdbcUrl())) {
            throw new MojoFailureException("请配置数据库URL地址");
        }
        //验证模板是否存在
        if (StringUtils.isNotBlank(getTemplate())) {
            String path = getRealFilePath(getTemplate());
            boolean exists = FileUtils.isFileExists(path);
            if (!exists) {
                throw new MojoFailureException("未检索到模板文件[" + path + "]");
            }
        }
    }

}
