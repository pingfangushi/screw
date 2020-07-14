/*
 * screw - 简洁好用的数据库表结构文档生成工具
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
package cn.smallbun.screw.dashboard.license;

/**
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/7/14 18:20
 */

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import cn.smallbun.screw.core.Version;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * License
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2019/7/11 17:13
 */
@Configuration
public class License implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {

	private static final AtomicBoolean PROCESSED = new AtomicBoolean(false);

	@Override
	public void onApplicationEvent(@NonNull ApplicationEnvironmentPreparedEvent event) {

		// Skip if processed before, prevent duplicated execution in Hierarchical ApplicationContext
		if (PROCESSED.get()) {
			return;
		}
		String chineseText = "smallbun screw 简洁好用的数据库表结构文档生成工具";
		String englishText = "smallbun screw simple and easy-to-use database table structure document generation tool";

		System.err.println(chineseText);
		System.err.println(englishText);
		System.err.println("screw-core Version: " + cn.smallbun.screw.core.Version.getVersion());
		System.err.println("screw-dashboard Version: " + Version.getVersion());
		// mark processed to be true
		PROCESSED.compareAndSet(false, true);
	}

	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE + 20 + 1;
	}
}
