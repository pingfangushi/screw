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
package cn.smallbun.screw.core.common;

import cn.smallbun.screw.core.util.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Properties
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/6/20 22:45
 */
public interface Properties {
    /**
     * 获取配置文件
     * @return {@link java.util.Properties}
     */
    String getConfigProperties();

    /**
     * 获取驱动
     * @return {@link String}
     */
    default String getDriver() throws IOException {
        java.util.Properties properties = getProperties();
        return properties.get("driver").toString();
    }

    /**
     * 获取URL
     * @return {@link String}
     */
    default String getUrl() throws IOException {
        java.util.Properties properties = getProperties();
        return properties.get("url").toString();
    }

    /**
     * 获取password
     * @return {@link String}
     */
    default String getPassword() throws IOException {
        java.util.Properties properties = getProperties();
        return properties.get("password").toString();
    }

    /**
     * 获取username
     * @return {@link String}
     */
    default String getUserName() throws IOException {
        java.util.Properties properties = getProperties();
        return properties.get("username").toString();
    }

    /**
     * 获取username
     * @return {@link String
     * @throws IOException
     */
    default String getSchema() throws IOException {
        java.util.Properties properties = getProperties();
        return properties.get("schema").toString();
    }

    /**
     * 获取properties内容
     * @return {@link java.util.Properties}
     * @throws IOException IOException
     */
    default java.util.Properties getProperties() throws IOException {
        File path = FileUtils.getFileByPath(getConfigProperties());
        java.util.Properties properties = new java.util.Properties();
        FileInputStream inputStream = new FileInputStream(Objects.requireNonNull(path));
        properties.load(inputStream);
        return properties;
    }
}
