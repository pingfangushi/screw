
<p align="center">
  <a href="https://github.com/pingfangushi/screw">
   <img alt="screw-logo" src="https://images.gitee.com/uploads/images/2020/0728/155335_59a712d2_1407605.png">
  </a>
</p>

<p align="center">💕 企业级开发过程中，一颗永不生锈的螺丝钉。</p>

<p align="center">
    <a href="https://github.com/pingfangushi/screw/blob/master/LICENSE">
        <img src="https://img.shields.io/badge/license-LGPL3-blue.svg" alt="LGPL3">
    </a>
    <a href="https://github.com/pingfangushi/screw">
        <img src="https://img.shields.io/badge/link-wiki-green.svg?style=flat-square" alt="wiki">
    </a>
    <a href="https://search.maven.org/search?q=cn.smallbun.screw">
       <img alt="Maven Central" src="https://img.shields.io/maven-central/v/cn.smallbun.screw/screw-core">
    </a>
    <a href="#">
        <img src="https://img.shields.io/badge/JDK-1.8+-green.svg" alt="JDK Version">
    </a>
    <a href="#">
        <img src="https://img.shields.io/badge/MAVEN-3.0+-green.svg" alt="JDK Version">
    </a>
</p>

>  🚀 screw (螺丝钉) 英:[`skruː`]  ~ 简洁好用的数据库表结构文档生成工具

## 简介

&emsp;&emsp;在企业级开发中、我们经常会有编写数据库表结构文档的时间付出，从业以来，待过几家企业，关于数据库表结构文档状态：要么没有、要么有、但都是手写、后期运维开发，需要手动进行维护到文档中，很是繁琐、如果忘记一次维护、就会给以后工作造成很多困扰、无形中制造了很多坑留给自己和后人，于是萌生了要自己写一个插件工具的想法，但由于自己前期在程序设计上没有很多造诣，且能力偏低，有想法并不能很好实现，随着工作阅历的增加，和知识的不断储备，终于在2020年的3月中旬开始进行编写，4月上旬完成初版，想完善差不多在开源，但由于工作太忙，业余时间不足，没有在进行完善，到了6月份由于工作原因、频繁设计和更改数据库、经常使用自己写的此插件、节省了很多时间，解决了很多问题 ，在仅有且不多的业余时间中、进行开源准备，于2020年6月22日，开源，欢迎大家使用、建议、并贡献。<br/>
&emsp;&emsp;关于名字，想一个太难了，好在我这个聪明的小脑瓜灵感一现，怎么突出它的小，但重要呢？从小就学过雷锋的螺丝钉精神，摘自雷锋日记：**虽然是细小的螺丝钉，是个细微的小齿轮，然而如果缺了它，那整个的机器就无法运转了，慢说是缺了它，即使是一枚小螺丝钉没拧紧，一个小齿轮略有破损，也要使机器的运转发生故障的...**，感觉自己写的这个工具，很有这意味，虽然很小、但是开发中缺了它还不行，于是便起名为**screw**（螺丝钉）。<br/>

## 特点

+ 简洁、轻量、设计良好

+ 多数据库支持
+ 多种格式文档
+ 灵活扩展
+ 支持自定义模板

## 数据库支持

- [x] MySQL 
- [x] MariaDB 
- [x] TIDB 
- [x] Oracle 
- [x] SqlServer 
- [x] PostgreSQL
- [x] Cache DB
- [ ] H2 （开发中）
- [ ] DB2  （开发中）
- [ ] HSQL  （开发中）
- [ ] SQLite（开发中）
- [ ] 瀚高（开发中）
- [ ] 达梦 （开发中）
- [ ] 虚谷  （开发中）
- [ ] 人大金仓（开发中）

## 文档生成支持

- [x] html
- [x] word
- [x] markdwon

## 文档截图

+ **html**

<p align="center">
   <img alt="HTML" src="https://images.gitee.com/uploads/images/2020/0622/161414_74cd0b68_1407605.png">
</p>
<p align="center">
   <img alt="screw-logo" src="https://images.gitee.com/uploads/images/2020/0622/161723_6da58c41_1407605.png">
</p>

+ **word**

<p align="center">
   <img alt="word" src="https://images.gitee.com/uploads/images/2020/0625/200946_1dc0717f_1407605.png">
</p>

+ **markdwon**

<p align="center">
   <img alt="markdwon" src="https://images.gitee.com/uploads/images/2020/0625/214749_7b15d8bd_1407605.png">
</p>
<p align="center">
   <img alt="markdwon" src="https://images.gitee.com/uploads/images/2020/0625/215006_3601e135_1407605.png">
</p>

## 使用方式

### 普通方式

+ **引入依赖**

```xml
<dependency>
    <groupId>cn.smallbun.screw</groupId>
    <artifactId>screw-core</artifactId>
    <version>${lastVersion}</version>
 </dependency>
```

+ **编写代码**

``` java
/**
 * 文档生成
 */
void documentGeneration() {
   //数据源
   HikariConfig hikariConfig = new HikariConfig();
   hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
   hikariConfig.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/database");
   hikariConfig.setUsername("root");
   hikariConfig.setPassword("password");
   //设置可以获取tables remarks信息
   hikariConfig.addDataSourceProperty("useInformationSchema", "true");
   hikariConfig.setMinimumIdle(2);
   hikariConfig.setMaximumPoolSize(5);
   DataSource dataSource = new HikariDataSource(hikariConfig);
   //生成配置
   EngineConfig engineConfig = EngineConfig.builder()
         //生成文件路径
         .fileOutputDir(fileOutputDir)
         //打开目录
         .openOutputDir(true)
         //文件类型
         .fileType(EngineFileType.HTML)
         //生成模板实现
         .produceType(EngineTemplateType.freemarker)
         //自定义文件名称
         .fileName("自定义文件名称").build();

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
		 .designatedTableSuffix(new ArrayList<>())
         //忽略表名
         .ignoreTableName(ignoreTableName)
         //忽略表前缀
         .ignoreTablePrefix(ignorePrefix)
         //忽略表后缀
         .ignoreTableSuffix(ignoreSuffix).build();
   //配置
   Configuration config = Configuration.builder()
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
   new DocumentationExecute(config).execute();
}
```

### Maven 插件

``` xml
<build>
    <plugins>
        <plugin>
            <groupId>cn.smallbun.screw</groupId>
            <artifactId>screw-maven-plugin</artifactId>
            <version>${lastVersion}</version>
            <dependencies>
                <!-- HikariCP -->
                <dependency>
                    <groupId>com.zaxxer</groupId>
                    <artifactId>HikariCP</artifactId>
                    <version>3.4.5</version>
                </dependency>
                <!--mysql driver-->
                <dependency>
                    <groupId>mysql</groupId>
                    <artifactId>mysql-connector-java</artifactId>
                    <version>8.0.20</version>
                </dependency>
            </dependencies>
            <configuration>
                <!--username-->
                <username>root</username>
                <!--password-->
                <password>password</password>
                <!--driver-->
                <driverClassName>com.mysql.cj.jdbc.Driver</driverClassName>
                <!--jdbc url-->
                <jdbcUrl>jdbc:mysql://127.0.0.1:3306/xxxx</jdbcUrl>
                <!--生成文件类型-->
                <fileType>HTML</fileType>
                <!--打开文件输出目录-->
                <openOutputDir>false</openOutputDir>
                <!--生成模板-->
                <produceType>freemarker</produceType>
                <!--文档名称 为空时:将采用[数据库名称-描述-版本号]作为文档名称-->
                <fileName>测试文档名称</fileName>
                <!--描述-->
                <description>数据库文档生成</description>
                <!--版本-->
                <version>${project.version}</version>
                <!--标题-->
                <title>数据库文档</title>
            </configuration>
            <executions>
                <execution>
                    <phase>compile</phase>
                    <goals>
                        <goal>run</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

### 使用文章

+ [SpringBoot整合screw生成数据库文档](https://my.oschina.net/mdxlcj/blog/4341399) 

+ [还在手动整理数据库文档？试试这个工具](https://mp.weixin.qq.com/s/Bo_U5_cl82hfQ6GmRs2vtA) 

+ [实用！一键生成数据库文档，堪称数据库界的Swagger](https://mp.weixin.qq.com/s/nPwFV7DN8Ogg54_crLlP3g) 

### 使用视频

+ [使用screw数据库文档生成工具快速生成数据库文档](https://www.bilibili.com/video/av456302504/)

+ [微人事一键生成数据库文档！炫！](https://mp.weixin.qq.com/s/rUde6XSGSG0jKuy0Wgf1Mw)

## 扩展模块

### pojo生成功能

#### 功能简介
&emsp;&emsp;pojo生成功能是基于screw延伸出的扩展模块,目前处于初步开发的状态。在日常的开发中,经过需求分析、建模之后,往往会先在数据库中建表,其次在进行代码的开发。那么pojo生成功能在这个阶段就可以帮助大家节省一些重复劳动了。使用pojo生成功能可以直接根据数据库生成对应的java pojo对象。这样后续的修改，开发都会很方便。

#### 数据库支持

- [x] MySQL 

#### 使用方式

+ **引入依赖**

```xml
<dependency>
    <groupId>cn.smallbun.screw</groupId>
    <artifactId>screw-extension</artifactId>
    <version>${lastVersion}</version>
 </dependency>
```

+ **编写代码**

``` java
/**
 * pojo生成
 */
void pojoGeneration() {
    //数据源
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
    hikariConfig.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/screw");
    hikariConfig.setUsername("screw");
    hikariConfig.setPassword("screw");
    //设置可以获取tables remarks信息
    hikariConfig.addDataSourceProperty("useInformationSchema", "true");
    hikariConfig.setMinimumIdle(2);
    hikariConfig.setMaximumPoolSize(5);
    DataSource dataSource = new HikariDataSource(hikariConfig);

    ProcessConfig processConfig = ProcessConfig.builder()
                                               //指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
                                               //根据名称指定表生成
                                               .designatedTableName(new ArrayList<>())
                                               //根据表前缀生成
                                               .designatedTablePrefix(new ArrayList<>())
                                               //根据表后缀生成
                                               .designatedTableSuffix(new ArrayList<>()).build();

    //设置生成pojo相关配置
    PojoConfiguration config = new PojoConfiguration();
    //设置文件存放路径
    config.setPath("/cn/smallbun/screw/");
    //设置包名
    config.setPackageName("cn.smallbun.screw");
    //设置是否使用lombok
    config.setUseLombok(false);
    //设置数据源
    config.setDataSource(dataSource);
    //设置命名策略
    config.setNameStrategy(new HumpNameStrategy());
    //设置表过滤逻辑
    config.setProcessConfig(processConfig);

    //也可以使用builder方式生成pojo相关配置
//    PojoConfiguration build = PojoConfiguration.builder()
//                                               .path("/Users/liuyu/IdeaProjects/MyselfStudy/template/src/main/java/com/haizhi/template/bean/dto/")
//                                               .packageName("com.haizhi.template.bean.dto")
//                                               .useLombok(true)
//                                               .dataSource(dataSource)
//                                               .nameStrategy(new HumpNameStrategy())
//                                               .processConfig(processConfig)
//                                               .build();

    //执行生成
    new PojoExecute(config).execute();
}
```



## 更多支持

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="center"><img src="https://images.gitee.com/uploads/images/2020/0622/161414_eaa2819d_1407605.jpeg" width="200" height="200"  alt="WeChat"/> </td>
    <td align="center"><img src="https://images.gitee.com/uploads/images/2020/0818/091246_8fb68a19_1407605.png" width="200" height="200"  alt="WeChat"/></td>
    <td align="center"><img src="https://images.gitee.com/uploads/images/2020/0707/191620_9a63fb23_1407605.png" width="200" height="200"  alt="QQ"/></td>
  </tr>
  <tr>
    <td align="center">微信公众号</td>
    <td align="center">微信交流群</td>
    <td align="center">QQ交流群</td>
  </tr>
</table>

+ 扫码关注官方微信公众号，第一时间尊享最新动向，回复 **screw** 获取作者微信号。

## 谁在使用

> 名称排序按登记先后，登记仅仅为了产品推广，希望出现您公司名称的小伙伴可以告诉我们。

 + 顺众数字科技（山东）有限公司
 + 江苏立华牧业股份有限公司
 + 上海德邦物流有限公司
 + 兰州百格网络科技有限公司
 
## 常见问题

 + 生成后文档乱码？

   MySQL：URL加入`?characterEncoding=UTF-8`。

 + Caused by: java.lang.NoSuchFieldError: VERSION_2_3_30？

   检查项目`freemarker`依赖，这是由于版本过低造成的，升级版本为`2.3.30`即可。

 + java.lang.AbstractMethodError: oracle.jdbc.driver.T4CConnection.getSchema()Ljava/lang/String;

   这是因为oracle驱动版本过低造成的，删除或屏蔽目前驱动版本，驱动添加升级为以下版本：
   
    ``` xml
    <dependency>
       <groupId>com.oracle.ojdbc</groupId>
       <artifactId>ojdbc8</artifactId>
       <version>19.3.0.0</version>
    </dependency>
    <dependency>
       <groupId>cn.easyproject</groupId>
       <artifactId>orai18n</artifactId>
       <version>12.1.0.2.0</version>
    </dependency>
    ```

 + MySQL数据库表和列字段有说明、生成文档没有说明？
   
   URL链接加入`useInformationSchema=true`即可。
   
 + java.lang.AbstractMethodError: com.mysql.jdbc.JDBC4Connection.getSchema()Ljava/lang/String;
    
   这是因为mysql驱动版本过低造成的，升级mysql驱动版本为最新即可。
   

## 推荐开源项目

| 项目名称       | 项目描述          |  
| ------------- |:-------------:|
|[api-boot](https://gitee.com/minbox-projects/api-boot)|为组件化构建Api服务而生| 

## 参与贡献

恳请的希望有兴趣的同学能够参与到**screw**建设中来，让我们共同完善它，让我们共同成长，帮助更多开发者，解决更多的问题。

## License

<img src='https://www.gnu.org/graphics/lgplv3-with-text-154x68.png' alt="license">

## 捐赠支持

&emsp;&emsp;我们一直致力于为您提供更好的数据库文档生成器。为了**screw**更好的发展和社区更加的繁荣，我们需要您的支持。<br/>
&emsp;&emsp;捐赠的目的是获得资金来维持我们所提供的程序和服务，捐款是基于大家自愿的原则，建议大家使用 支付宝 的"转账付款"功能，即时到帐。您的支持是鼓励我们前行的动力，无论金额多少都足够表达您这份心意。<br/>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="center"><img src="https://images.gitee.com/uploads/images/2020/0622/161414_c87e1846_1407605.png" width="200" height="200" /> </td>
    <td align="center"><img src="https://images.gitee.com/uploads/images/2020/0622/161414_e953f85f_1407605.png" width="200" height="200" /></td>
  </tr>
  <tr>
    <td align="center">支付宝</td>
    <td align="center">微信</td>
  </tr>
</table>



>**screw** 感谢您选择相信和支持！