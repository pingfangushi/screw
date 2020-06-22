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
package cn.smallbun.screw.core.execute;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineFactory;
import cn.smallbun.screw.core.engine.TemplateEngine;
import cn.smallbun.screw.core.exception.BuilderException;
import cn.smallbun.screw.core.metadata.model.DataModel;
import cn.smallbun.screw.core.process.DataModelProcess;
import cn.smallbun.screw.core.util.ExceptionUtils;

/**
 * 文档生成
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/4/1 22:51
 */
public class DocumentationExecute extends AbstractExecute {

    public DocumentationExecute(Configuration config) {
        super(config);
    }

    /**
     * 执行
     *
     * @throws BuilderException BuilderException
     */
    @Override
    public void execute() throws BuilderException {
        try {
            long start = System.currentTimeMillis();
            //处理数据
            DataModel dataModel = new DataModelProcess(config).process();
            //产生文档
            TemplateEngine produce = new EngineFactory(config.getEngineConfig()).newInstance();
            produce.produce(dataModel, getDocName(dataModel.getDatabase()));
            logger.debug("database document generation complete time consuming:{}ms",
                System.currentTimeMillis() - start);
        } catch (Exception e) {
            throw ExceptionUtils.mpe(e);
        }
    }
}
