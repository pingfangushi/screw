package main.java.cn.smallbun.screw.idea.screw;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import cn.smallbun.screw.idea.model.DataSourceConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;

public class ScrewExecutor {

    public static void executor(DataSourceConfig config) {
        //数据源
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(config.getDriver());
        hikariConfig.setJdbcUrl(config.getUrl());
        hikariConfig.setUsername(config.getUserName());
        hikariConfig.setPassword(config.getPassword());
        //设置可以获取tables remarks信息
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaximumPoolSize(5);
        DataSource dataSource = new HikariDataSource(hikariConfig);
        //生成配置
        EngineConfig engineConfig = EngineConfig.builder()
                //生成文件路径
                .fileOutputDir(config.getFilePath())
                //打开目录
                .openOutputDir(config.getOpen())
                //文件类型
                .fileType(config.getEngineFileType())
                //生成模板实现
                .produceType(EngineTemplateType.freemarker)
                //自定义文件名称
                .fileName(config.getFileName()).build();

        //忽略表
        ArrayList<String> ignoreTableName = new ArrayList<>();
        ignoreTableName.add("test_user");
        ignoreTableName.add("test_group");
        //忽略表前缀
        ArrayList<String> ignorePrefix = new ArrayList<>();
        ignorePrefix.add("test_");
        //忽略表后缀
        ArrayList<String> ignoreSuffix = new ArrayList<>();
        ignoreSuffix.add("_test");
        ProcessConfig processConfig = ProcessConfig.builder()
                //指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
                //根据名称指定表生成
                .designatedTableName(new ArrayList<>())
                //根据表前缀生成
                .designatedTablePrefix(new ArrayList<>())
                //根据表后缀生成
                .designatedTableSuffix(new ArrayList<>()).build();
        if (config.getIgnoreTable()!=null&&config.getIgnoreTable().size()>0) {
            processConfig.setIgnoreTableName(config.getIgnoreTable());
        }
        if (config.getIgnorePrefix() != null && config.getIgnorePrefix().size() > 0) {
            processConfig.setIgnoreTablePrefix(config.getIgnorePrefix());
        }
        if (config.getIgnoreSuffix() != null && config.getIgnoreSuffix().size() > 0) {
            processConfig.setDesignatedTableSuffix(config.getIgnoreSuffix());
        }
        //配置
        Configuration configuration = Configuration.builder()
                //版本
                .version("1.0.0")
                //描述
                .description("数据库设计文档生成")
                //数据源
                .dataSource(dataSource)
                //生成配置
                .engineConfig(engineConfig)
                //生成配置
                .produceConfig(processConfig)
                .build();
        //执行生成
        new DocumentationExecute(configuration).execute();
    }

}
