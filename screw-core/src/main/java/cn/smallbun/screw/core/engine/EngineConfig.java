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
package cn.smallbun.screw.core.engine;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 文件生成配置
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/17 18:19
 */
@Data
@Builder
public class EngineConfig implements Serializable {
    /**
     * 是否打开输出目录
     */
    private boolean            openOutputDir;
    /**
     * 文件产生位置
     */
    private String             fileOutputDir;
    /**
     * 生成文件类型
     */
    private EngineFileType     fileType;
    /**
     * 生成实现
     */
    private EngineTemplateType produceType;
    /**
     * 自定义模板，模板需要和文件类型和使用模板的语法进行编写和处理，否则将会生成错误
     */
    private String             customTemplate;
    /**
     * 文件名称
     */
    private String             fileName;
}
