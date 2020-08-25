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
package cn.smallbun.screw.extension.pojo;

import cn.smallbun.screw.core.process.ProcessConfig;
import cn.smallbun.screw.extension.pojo.strategy.NameStrategy;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liu·yu
 * Created by 15952866402@163.com on 2020-08-14
 */
@Data
@NoArgsConstructor
public class PojoConfiguration {

    /**
     * 包名
     */
    private String                packageName;

    /**
     * 生成文件路径
     */
    private String                path;

    /**
     * 是否使用lombok
     */
    private boolean               useLombok;

    /**
     * 数据源对象
     */
    private DataSource            dataSource;

    /**
     * 生成配置
     */
    private ProcessConfig         processConfig;

    /**
     * 命名策略
     */
    private NameStrategy          nameStrategy;

    /**
     * 自定义类型转换
     */
    private Map<String, Class<?>> customType;

    /**
     * builder
     * @return {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final PojoConfiguration configuration;

        public Builder() {
            this.configuration = new PojoConfiguration();
        }

        public Builder packageName(String packageName) {
            this.configuration.setPackageName(packageName);
            return this;
        }

        public Builder path(String path) {
            this.configuration.setPath(path);
            return this;
        }

        public Builder useLombok(boolean useLombok) {
            this.configuration.setUseLombok(useLombok);
            return this;
        }

        public Builder dataSource(DataSource dataSource) {
            this.configuration.setDataSource(dataSource);
            return this;
        }

        public Builder processConfig(ProcessConfig processConfig) {
            this.configuration.setProcessConfig(processConfig);
            return this;
        }

        public Builder nameStrategy(NameStrategy strategy) {
            this.configuration.setNameStrategy(strategy);
            return this;
        }

        public Builder customType(String type, Class<?> clazz) {
            if (configuration.getCustomType() == null) {
                configuration.setCustomType(new HashMap<>());
            }
            configuration.getCustomType().put(type, clazz);
            return this;
        }

        public PojoConfiguration build() {
            return this.configuration;
        }

    }

}
