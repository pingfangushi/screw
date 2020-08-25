package cn.smallbun.screw.core.metadata;

import java.io.Serializable;

/**
 * 列长度
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/8/25 10:53
 */
public interface ColumnLength extends Serializable {
    /**
     * 表名
     *
     * @return {@link String}
     */
    String getTableName();

    /**
     * 列名
     *
     * @return {@link String}
     */
    String getColumnName();

    /**
     * 列长度
     *
     * @return {@link String}
     */
    String getColumnLength();
}
