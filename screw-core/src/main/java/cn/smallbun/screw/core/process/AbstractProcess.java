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
package cn.smallbun.screw.core.process;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.metadata.Column;
import cn.smallbun.screw.core.metadata.PrimaryKey;
import cn.smallbun.screw.core.metadata.Table;
import cn.smallbun.screw.core.metadata.model.ColumnModel;
import cn.smallbun.screw.core.metadata.model.DataModel;
import cn.smallbun.screw.core.metadata.model.TableModel;
import cn.smallbun.screw.core.util.Assert;
import cn.smallbun.screw.core.util.BeanUtils;
import cn.smallbun.screw.core.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static cn.smallbun.screw.core.util.BeanUtils.*;

/**
 * AbstractBuilder
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/22 21:09
 */
public abstract class AbstractProcess implements Process {
    /**
     * LOGGER
     */
    final Logger                                logger             = LoggerFactory
        .getLogger(this.getClass());
    /**
     * 表信息缓存
     */
    volatile Map<String, List<? extends Table>> tablesCaching      = new ConcurrentHashMap<>();
    /**
     * 列信息缓存
     */
    volatile Map<String, List<Column>>          columnsCaching     = new ConcurrentHashMap<>();
    /**
     * 主键信息缓存
     */
    volatile Map<String, List<PrimaryKey>>      primaryKeysCaching = new ConcurrentHashMap<>();

    /**
     * Configuration
     */
    protected Configuration                     config;

    private AbstractProcess() {
    }

    /**
     * 构造方法
     *
     * @param configuration     {@link Configuration}
     */
    protected AbstractProcess(Configuration configuration) {
        Assert.notNull(configuration, "Configuration can not be empty!");
        this.config = configuration;
    }

    /**
     * 过滤表
     * 存在指定生成和指定不生成，优先级为：如果指定生成，只会生成指定的表，未指定的不会生成，也不会处理忽略表
     *
     * @param tables {@link List} 处理前数据
     * @return {@link List} 处理过后的数据
     * @see "1.0.3"
     */
    protected List<TableModel> filterTables(List<TableModel> tables) {
        ProcessConfig produceConfig = config.getProduceConfig();
        if (!Objects.isNull(config) && !Objects.isNull(config.getProduceConfig())) {
            //指定生成的表名、前缀、后缀任意不为空，按照指定表生成，其余不生成，不会在处理忽略表
            if (CollectionUtils.isNotEmpty(produceConfig.getDesignatedTableName())
                //前缀
                || CollectionUtils.isNotEmpty(produceConfig.getDesignatedTablePrefix())
                //后缀
                || CollectionUtils.isNotEmpty(produceConfig.getDesignatedTableSuffix())) {
                return handleDesignated(tables);
            }
            //处理忽略表
            else {
                return handleIgnore(tables);
            }
        }
        return tables;
    }

    /**
     * 处理指定表
     *
     * @param tables {@link List} 处理前数据
     * @return {@link List} 处理过后的数据
     * @see "1.0.3"
     */
    private List<TableModel> handleDesignated(List<TableModel> tables) {
        List<TableModel> tableModels = new ArrayList<>();
        ProcessConfig produceConfig = this.config.getProduceConfig();
        if (!Objects.isNull(config) && !Objects.isNull(produceConfig)) {
            //指定表名
            if (CollectionUtils.isNotEmpty(produceConfig.getDesignatedTableName())) {
                List<String> list = produceConfig.getDesignatedTableName();
                for (String name : list) {
                    tableModels.addAll(tables.stream().filter(j -> j.getTableName().equals(name))
                        .collect(Collectors.toList()));
                }
            }
            //指定表名前缀
            if (CollectionUtils.isNotEmpty(produceConfig.getDesignatedTablePrefix())) {
                List<String> list = produceConfig.getDesignatedTablePrefix();
                for (String prefix : list) {
                    tableModels
                        .addAll(tables.stream().filter(j -> j.getTableName().startsWith(prefix))
                            .collect(Collectors.toList()));
                }
            }
            //指定表名后缀
            if (CollectionUtils.isNotEmpty(produceConfig.getDesignatedTableSuffix())) {
                List<String> list = produceConfig.getDesignatedTableSuffix();
                for (String suffix : list) {
                    tableModels
                        .addAll(tables.stream().filter(j -> j.getTableName().endsWith(suffix))
                            .collect(Collectors.toList()));
                }
            }
            return tableModels;
        }
        return tableModels;
    }

    /**
     * 处理忽略
     *
     * @param tables {@link List} 处理前数据
     * @return {@link List} 处理过后的数据
     */
    private List<TableModel> handleIgnore(List<TableModel> tables) {
        ProcessConfig produceConfig = this.config.getProduceConfig();
        if (!Objects.isNull(this.config) && !Objects.isNull(produceConfig)) {
            //处理忽略表名
            if (CollectionUtils.isNotEmpty(produceConfig.getIgnoreTableName())) {
                List<String> list = produceConfig.getIgnoreTableName();
                for (String name : list) {
                    tables = tables.stream().filter(j -> !j.getTableName().equals(name))
                        .collect(Collectors.toList());
                }
            }
            //忽略表名前缀
            if (CollectionUtils.isNotEmpty(produceConfig.getIgnoreTablePrefix())) {
                List<String> list = produceConfig.getIgnoreTablePrefix();
                for (String prefix : list) {
                    tables = tables.stream().filter(j -> !j.getTableName().startsWith(prefix))
                        .collect(Collectors.toList());
                }
            }
            //忽略表名后缀
            if (CollectionUtils.isNotEmpty(produceConfig.getIgnoreTableSuffix())) {
                List<String> list = produceConfig.getIgnoreTableSuffix();
                for (String suffix : list) {
                    tables = tables.stream().filter(j -> !j.getTableName().endsWith(suffix))
                        .collect(Collectors.toList());
                }
            }
            return tables;
        }
        return tables;
    }

    /**
     *  优化数据
     * @param dataModel {@link DataModel}
     * @see "1.0.3"
     */
    public void optimizeData(DataModel dataModel) {
        //trim
        beanAttributeValueTrim(dataModel);
        //tables
        List<TableModel> tables = dataModel.getTables();
        //columns
        tables.forEach(i -> {
            //table escape xml
            beanAttributeValueTrim(i);
            List<ColumnModel> columns = i.getColumns();
            //columns escape xml
            columns.forEach(BeanUtils::beanAttributeValueTrim);
        });
        //if file type is word
        if (config.getEngineConfig().getFileType().equals(EngineFileType.WORD)) {
            //escape xml
            beanAttributeValueEscapeXml(dataModel);
            //tables
            tables.forEach(i -> {
                //table escape xml
                beanAttributeValueEscapeXml(i);
                List<ColumnModel> columns = i.getColumns();
                //columns escape xml
                columns.forEach(BeanUtils::beanAttributeValueEscapeXml);
            });
        }
        //if file type is markdown
        if (config.getEngineConfig().getFileType().equals(EngineFileType.MD)) {
            //escape xml
            beanAttributeValueReplaceBlank(dataModel);
            //columns
            tables.forEach(i -> {
                //table escape xml
                beanAttributeValueReplaceBlank(i);
                List<ColumnModel> columns = i.getColumns();
                //columns escape xml
                columns.forEach(BeanUtils::beanAttributeValueReplaceBlank);
            });
        }
    }
}
