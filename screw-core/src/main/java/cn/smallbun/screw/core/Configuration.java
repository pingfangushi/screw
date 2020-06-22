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
package cn.smallbun.screw.core;

import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.process.ProcessConfig;
import cn.smallbun.screw.core.util.Assert;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.sql.DataSource;
import java.io.Serializable;

/**
 * Screw 配置入口
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on  2020/3/19
 */
@Data
@NoArgsConstructor
public class Configuration implements Serializable {

    /**
     * 组织
     */
    private String        organization;
    /**
     * url
     */
    private String        organizationUrl;
    /**
     * 标题
     */
    private String        title;
    /**
     * 版本号
     */
    private String        version;
    /**
     * 描述
     */
    private String        description;
    /**
     * 数据源，这里直接使用@see{@link DataSource}接口，好处就，可以使用任何数据源
     */
    private DataSource    dataSource;
    /**
     * 生成配置
     */
    private ProcessConfig produceConfig;
    /**
     * 引擎配置，关于数据库文档生成相关配置
     */
    private EngineConfig  engineConfig;

    /**
     * 构造函数
     *
     * @param title         {@link String} 标题
     * @param organization  {@link String} 机构
     * @param version       {@link String} 版本
     * @param description   {@link String} 描述
     * @param dataSource    {@link DataSource} 数据源
     * @param produceConfig {@link ProcessConfig} 生成配置
     * @param engineConfig  {@link EngineConfig} 生成配置
     */
    private Configuration(String organization, String organizationUrl, String title, String version,
                          String description, DataSource dataSource, ProcessConfig produceConfig,
                          EngineConfig engineConfig) {
        Assert.notNull(dataSource, "DataSource can not be empty!");
        Assert.notNull(engineConfig, "EngineConfig can not be empty!");
        this.title = title;
        this.organizationUrl = organizationUrl;
        this.organization = organization;
        this.version = version;
        this.description = description;
        this.dataSource = dataSource;
        this.engineConfig = engineConfig;
        this.produceConfig = produceConfig;
    }

    /**
     * builder
     *
     * @return {@link Configuration.ConfigurationBuilder}
     */
    public static Configuration.ConfigurationBuilder builder() {
        return new Configuration.ConfigurationBuilder();
    }

    @ToString
    public static class ConfigurationBuilder {
        private String        organization;
        private String        organizationUrl;
        private String        title;
        private String        version;
        private String        description;
        private DataSource    dataSource;
        private ProcessConfig produceConfig;
        private EngineConfig  engineConfig;

        ConfigurationBuilder() {
        }

        public Configuration.ConfigurationBuilder organization(String org) {
            this.organization = org;
            return this;
        }

        public Configuration.ConfigurationBuilder organizationUrl(String orgUrl) {
            this.organizationUrl = orgUrl;
            return this;
        }

        public Configuration.ConfigurationBuilder title(String title) {
            this.title = title;
            return this;
        }

        public Configuration.ConfigurationBuilder version(String version) {
            this.version = version;
            return this;
        }

        public Configuration.ConfigurationBuilder description(String description) {
            this.description = description;
            return this;
        }

        public Configuration.ConfigurationBuilder dataSource(DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public Configuration.ConfigurationBuilder produceConfig(ProcessConfig processConfig) {
            this.produceConfig = processConfig;
            return this;
        }

        public Configuration.ConfigurationBuilder engineConfig(EngineConfig engineConfig) {
            this.engineConfig = engineConfig;
            return this;
        }

        /**
         * build
         *
         * @return {@link Configuration}
         */
        public Configuration build() {
            return new Configuration(this.organization, this.organizationUrl, this.title,
                this.version, this.description, this.dataSource, this.produceConfig,
                this.engineConfig);
        }
    }
}
