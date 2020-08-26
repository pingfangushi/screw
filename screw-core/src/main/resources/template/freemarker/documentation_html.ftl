<#--

    screw-core - 简洁好用的数据库表结构文档生成工具
    Copyright © 2020 SanLi (qinggang.zuo@gmail.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

-->
﻿<html lang="zh"><head><title>${title!'数据库设计文档'}</title><style type='text/css'>body {            padding-bottom: 50px        }        body, td {            font-family: verdana, fantasy;            font-size: 12px;            line-height: 150%        }        table {            width: 100%;            background-color: #ccc;            margin: 5px 0        }        td {            background-color: #fff;            padding: 3px 3px 3px 10px        }        thead td {            text-align: center;            font-weight: bold;            background-color: #eee        }        a:link, a:visited, a:active {            color: #015fb6;            text-decoration: none        }        a:hover {            color: #e33e06        }</style></head><body style='text-align:center;'><div style='width:800px; margin:20px auto; text-align:left;'><a name='index'></a><h2 style='text-align:center; line-height:50px;'>${title!'数据库设计文档'}</h2><div><b>数据库名：${database!''}</b><br><#if (version)??><b>文档版本：${version!''}</b><br></#if><#if (description)??><b>文档描述：${description!''}</b><br></#if></div><table cellspacing='1'><thead><tr><td style='width:40px; '>序号</td><td>表名</td><td>说明</td></tr></thead><#list tables><#items as t><tr><td style='text-align:center;'>${t?index+1}</td><td><a href='#${t.tableName}'>${t.tableName}</a></td><td>${t.remarks!''}</td></tr></#items></#list></table><#list tables><#items as t><a name='${t.tableName}'></a><div style='margin-top:30px;'><a href='#index'                                         style='float:right; margin-top:6px;'>返回目录</a><b>表名：${t.tableName}</b></div><div>说明：${t.remarks!''}</div><div>数据列：</div><table cellspacing='1'><thead><tr><td style='width:40px; '>序号</td><td>名称</td><td>数据类型</td><td>小数位</td><td>允许空值</td><td>主键</td><td>默认值</td><td>说明</td></tr></thead><#list t.columns><#items as c><tr><td style='text-align:center;'>${c?index+1}</td><td>${c.columnName!''}</td><td align='center'>${c.columnType!''}</td><td align='center'>${c.decimalDigits!'0'}</td><td align='center'>${c.nullable!''}</td><td align='center'>${c.primaryKey!''}</td><td align='center'>${c.columnDef!''}</td><td align='center'>${c.remarks!''}</td></tr></#items></#list></table></#items></#list></div><footer><#--机构信息--><#if (organization)??><div><a href="${organizationUrl!'#'}">${organization!''}</a></div></#if></footer></body></html>