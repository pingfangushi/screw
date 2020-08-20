package ${packageName};

<#if importList?? && (importList?size > 0) >
<#list  importList>
    <#items as import>
import ${import};
    </#items>
</#list>
</#if>
<#if useLombok>
import lombok.Data;
</#if>

/**
 * 数据库表名: ${tableName}
<#if (remarks)??&&remarks?length gt 0>
 * 数据库表注释: ${remarks}
</#if>
 */
<#if useLombok>
@Data
</#if>
public class ${className} {
<#list fieldList>

    <#items as field>
    <#if (field.remarks)??&&field.remarks?length gt 0>
    /**数据库字段注释: ${field.remarks}*/
    </#if>
    /**数据库字段类型: ${field.fieldType},数据库字段名: ${field.fieldName}**/
    private ${field.classType} ${field.className};

    </#items>
</#list>
<#if !useLombok>
    <#list fieldList>
    <#items as field>
    public ${field.classType} ${field.getName}(){
        return this.${field.className};
    }

    public void ${field.setName}(${field.classType} ${field.className}){
        this.${field.className} = ${field.className};
    }

    </#items>
    </#list>
</#if>
}
