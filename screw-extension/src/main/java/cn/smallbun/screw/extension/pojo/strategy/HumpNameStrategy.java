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
package cn.smallbun.screw.extension.pojo.strategy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 采用驼峰命名策略(推荐数据库字段是下划线命名的情况下使用)
 *
 * @author liu·yu
 * Created by 15952866402@163.com on 2020-08-17
 */
public class HumpNameStrategy implements NameStrategy {

    private final Pattern linePattern = Pattern.compile("_(\\w)");

    @Override
    public String transClassName(String name) {
        return upperCase(lineToHump(name));
    }

    @Override
    public String transFieldName(String name, Class<?> type) {
        return lineToHump(name);
    }

    @Override
    public String transSetName(String name, Class<?> type) {
        return "set" + upperCase(lineToHump(name));
    }

    @Override
    public String transGetName(String name, Class<?> type) {
        if (Boolean.class.isAssignableFrom(type)) {
            return "is" + upperCase(lineToHump(name));
        }
        return "get" + upperCase(lineToHump(name));
    }

    /**
     * 下划线转驼峰
     */
    private String lineToHump(String str) {
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 首字母转大写
     *
     * @param str {@link String}
     * @return  {@link String}
     */
    private String upperCase(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

}
