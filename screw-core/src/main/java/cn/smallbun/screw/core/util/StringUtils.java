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
package cn.smallbun.screw.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/30 22:21
 */
public class StringUtils {
    private StringUtils() {
    }

    /**
     * 安全的进行字符串 format
     *
     * @param target 目标字符串
     * @param params format 参数
     * @return format 后的
     */
    public static String format(String target, Object... params) {
        return String.format(target, params);
    }

    public static boolean isBlank(final CharSequence cs) {
        if (cs == null) {
            return true;
        }
        int l = cs.length();
        if (l > 0) {
            for (int i = 0; i < l; i++) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * 去除换行
     */
    static final Pattern REPLACE_BLANK_PATTERN = Pattern.compile("\\s*|\t|\r|\n");

    /**
     * replaceBlank
     * @param str {@link String}
     * @return {@link String}
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Matcher m = REPLACE_BLANK_PATTERN.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 如果为空默认
     *
     * @param str        {@link String}
     * @param defaultStr {@link String}
     * @return {@link String}
     */
    public static String defaultString(final String str, final String defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }
}
