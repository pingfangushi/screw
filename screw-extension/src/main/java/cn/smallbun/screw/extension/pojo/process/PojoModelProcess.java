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
package cn.smallbun.screw.extension.pojo.process;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.metadata.model.ColumnModel;
import cn.smallbun.screw.core.metadata.model.DataModel;
import cn.smallbun.screw.core.metadata.model.TableModel;
import cn.smallbun.screw.core.process.DataModelProcess;
import cn.smallbun.screw.extension.pojo.PojoConfiguration;
import cn.smallbun.screw.extension.pojo.dialect.TypeDialect;
import cn.smallbun.screw.extension.pojo.dialect.TypeDialectFactory;
import cn.smallbun.screw.extension.pojo.metadata.model.PojoModel;
import cn.smallbun.screw.extension.pojo.metadata.model.TypeModel;
import cn.smallbun.screw.extension.pojo.strategy.NameStrategy;

import java.util.*;

/**
 * 用于生成pojo的数据
 *
 * @author liu·yu
 * Created by 15952866402@163.com on 2020-08-14
 */
public class PojoModelProcess implements PojoProcess {

    private final PojoConfiguration pojoConfiguration;

    /**
     * 构造方法
     *
     * @param configuration {@link Configuration}
     */
    public PojoModelProcess(PojoConfiguration configuration) {
        this.pojoConfiguration = configuration;
    }

    @Override
    public List<PojoModel> getPojoModel() {
        //获取数据库元数据
        List<TableModel> tableModels = getTableModel(pojoConfiguration);

        //新建list，准备接收结果集
        List<PojoModel> pojoModels = new ArrayList<>(tableModels.size());

        //获取类型转换器
        TypeDialect dialect = new TypeDialectFactory(pojoConfiguration.getDataSource())
            .newInstance();

        //获取命名策略,用于转换表至对象，列至字段的名称策略
        NameStrategy nameStrategy = pojoConfiguration.getNameStrategy();

        //获取用户自定义的类型转换
        Map<String, Class<?>> customType = pojoConfiguration.getCustomType();

        //进行数据转换
        for (TableModel model : tableModels) {
            PojoModel pojoModel = new PojoModel();
            pojoModel.setUseLombok(pojoConfiguration.isUseLombok());

            pojoModel.setPackageName(pojoConfiguration.getPackageName());
            pojoModel.setTableName(model.getTableName());
            pojoModel.setRemarks(model.getRemarks());
            pojoModel.setClassName(nameStrategy.transClassName(model.getTableName()));

            //需要import的属性
            Set<String> importList = new HashSet<>();
            List<TypeModel> fieldList = new ArrayList<>();

            for (ColumnModel column : model.getColumns()) {
                TypeModel typeModel = new TypeModel();
                typeModel.setFieldName(column.getColumnName());
                typeModel.setFieldType(column.getTypeName());
                typeModel.setRemarks(column.getRemarks());

                //先判断用户是否自定义
                Class<?> classType = dialect.getTypeByMap(customType, column.getTypeName());
                if (classType == null) {
                    classType = dialect.getClassTypeByFieldType(column.getTypeName());
                }

                //如果对象不在java.lang包下，需要import
                if (!classType.getTypeName().startsWith("java.lang")) {
                    importList.add(classType.getTypeName());
                }

                typeModel
                    .setClassName(nameStrategy.transFieldName(column.getColumnName(), classType));
                typeModel.setClassType(classType.getSimpleName());

                //如果不使用lombok，需要生成get，set方法
                if (!pojoConfiguration.isUseLombok()) {
                    typeModel
                        .setGetName(nameStrategy.transGetName(column.getColumnName(), classType));
                    typeModel
                        .setSetName(nameStrategy.transSetName(column.getColumnName(), classType));
                }

                fieldList.add(typeModel);
            }
            pojoModel.setImportList(importList);
            pojoModel.setFieldList(fieldList);
            pojoModels.add(pojoModel);
        }
        return pojoModels;
    }

    private List<TableModel> getTableModel(PojoConfiguration pojoConfiguration) {
        //配置
        Configuration config = Configuration.builder().dataSource(pojoConfiguration.getDataSource())
            .produceConfig(pojoConfiguration.getProcessConfig())
            .engineConfig(EngineConfig.builder().fileType(EngineFileType.HTML).build()).build();

        DataModel process = new DataModelProcess(config).process();
        return process.getTables();
    }
}
