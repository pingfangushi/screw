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

import cn.smallbun.screw.core.exception.ScrewException;

/**
 * 异常工具类
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/30 22:19
 */
public class ExceptionUtils {
    private ExceptionUtils() {
    }

    /**
     * 返回一个新的异常，统一构建，方便统一处理
     *
     * @param msg    {@link String} 消息
     * @param t      {@link Throwable} 异常信息
     * @param params {@link String} 参数
     * @return ScrewException ScrewException
     */
    public static ScrewException mpe(String msg, Throwable t, Object... params) {
        return new ScrewException(StringUtils.format(msg, params), t);
    }

    /**
     * 重载的方法
     *
     * @param msg    {@link String} 消息
     * @param params {@link Object} 参数
     * @return ScrewException ScrewException
     */
    public static ScrewException mpe(String msg, Object... params) {
        return new ScrewException(StringUtils.format(msg, params));
    }

    /**
     * 重载的方法
     *
     * @param t {@link Throwable} 异常
     * @return ScrewException  ScrewException
     */
    public static ScrewException mpe(Throwable t) {
        return new ScrewException(t);
    }
}
