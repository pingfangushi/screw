package main.java.cn.smallbun.screw.idea.action;

import cn.smallbun.screw.idea.wrapper.ScrewWrapper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

public class ScrewAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        Project project = e.getProject();
        ScrewWrapper screwWrapper = new ScrewWrapper(project);
        screwWrapper.setResizable(true);
        screwWrapper.showAndGet();
    }
}
