package main.java.cn.smallbun.screw.idea.wrapper;

import cn.smallbun.screw.idea.model.ConfigStatement;
import cn.smallbun.screw.idea.notify.ScrewNotifier;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import cn.smallbun.screw.idea.model.DataSourceConfig;
import cn.smallbun.screw.idea.model.DataSourceEnum;
import cn.smallbun.screw.idea.model.EngineFileTypeEnum;
import cn.smallbun.screw.idea.screw.ScrewExecutor;
import cn.smallbun.screw.idea.ui.DatasourcePanel;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class ScrewWrapper extends DialogWrapper {

    DatasourcePanel datasourcePanel = new DatasourcePanel();
    private Project project;
    public ScrewWrapper(@Nullable Project project) {
        super(project);
        this.project = project;
        init();

        setTitle("自动生成数据库文档");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        datasourcePanel.pack();

        return datasourcePanel.getCenter();
    }

    @Override
    protected void doOKAction() {
        DataSourceConfig config = new DataSourceConfig();
        if (StringUtils.isNoneEmpty(datasourcePanel.getUrl().getText())) {
            config.setUrl(datasourcePanel.getUrl().getText());
        }
        if (datasourcePanel.getDriver().getSelectedItem() != null) {
            String datasource = (String) datasourcePanel.getDriver().getSelectedItem();
            config.setDriver(DataSourceEnum.getDatabase(datasource));

        }
        if (StringUtils.isNoneEmpty(datasourcePanel.getUsername().getText())) {
            config.setUserName(datasourcePanel.getUsername().getText());
        }
        if (StringUtils.isNoneEmpty(datasourcePanel.getPassword().getText())) {
            config.setPassword(datasourcePanel.getPassword().getText());
        }

        if (StringUtils.isNoneEmpty(datasourcePanel.getFilename().getText())) {
            config.setFileName(datasourcePanel.getFilename().getText());
        }
        if (StringUtils.isNoneEmpty(datasourcePanel.getDesc().getText())) {
            config.setDesc(datasourcePanel.getDesc().getText());
        }
        if (datasourcePanel.getPattern().getSelectedItem() != null) {
            String item = (String) datasourcePanel.getPattern().getSelectedItem();
            EngineFileTypeEnum engineFileTypeEnum = EngineFileTypeEnum.getByName(item);
            if (engineFileTypeEnum != null) {
                config.setEngineFileType(engineFileTypeEnum.getEngineFileType());
            }
        }
        if (StringUtils.isNoneEmpty(datasourcePanel.getIgnoreTable().getText())) {
            String tableStr = datasourcePanel.getIgnoreTable().getText();
            List<String> ignoreTable = Arrays.asList(tableStr.split("\n"));
            config.setIgnoreTable(ignoreTable);
        }
        if ( StringUtils.isNoneEmpty(datasourcePanel.getIgnorePrefix().getText())) {
            String prefix = datasourcePanel.getIgnorePrefix().getText();
            List<String> ignorePrefix = Arrays.asList(prefix.split("\n"));
            config.setIgnorePrefix(ignorePrefix);
        }
        if (StringUtils.isNoneEmpty(datasourcePanel.getIgnoreSuffix().getText())) {
            String suffix = datasourcePanel.getIgnoreSuffix().getText();
            List<String> ignoreSuffix = Arrays.asList(suffix.split("\n"));
            config.setIgnoreSuffix(ignoreSuffix);
        }
        if (datasourcePanel.getIsOpen().isSelected()) {
            config.setOpen(true);
        }
        ConfigStatement.setDataSourceConfig(config);
        FileChooserDescriptor singleFileDescriptor = new FileChooserDescriptor(false, true, false, false, false, false);

        VirtualFile virtualFile = FileChooser.chooseFile(singleFileDescriptor, project, null);
        config.setFilePath(virtualFile.getPath());
        try {
            ScrewExecutor.executor(config);
        }catch (Exception e){
            ScrewNotifier screwNotifier = new ScrewNotifier();
            screwNotifier.notify("生成文档异常,异常原因为" + e.getMessage());
        }finally {
            super.doOKAction();
        }
    }
}
