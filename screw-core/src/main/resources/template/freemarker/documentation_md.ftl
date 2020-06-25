# ${title!'数据库设计文档'}

**数据库名：** ${database!''}
**文档版本：** ${version!''}
**文档描述：** ${description!''}
| 表名                  | 说明       |
| :-------------------- | :--------- |
<#list tables>
    <#items as t>
        | [${t.tableName!''}](#${t.tableName!''}) | ${t.remarks!''} |
    </#items>
</#list>
<#list tables><#items as t>
**表名：** <a id="${t.tableName!''}">${t.tableName!''}</a>

**说明：** ${t.remarks!''}

**数据列：**

<#list t.columns>
| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :--: | :--- | :------: | :----: | :----: | :------: | :--: | :----: | :--: |
<#items as c>
|  ${c?index+1}   | ${c.columnName!''} |   ${c.typeName!''}   | ${c.columnSize!''} |   ${c.decimalDigits!'0'}    |    ${c.nullable!''}     |  ${c.primaryKey!''}   |   ${c.columnDef!''}    | ${c.remarks!''}  |
</#items>
</#list></#items>
</#list>