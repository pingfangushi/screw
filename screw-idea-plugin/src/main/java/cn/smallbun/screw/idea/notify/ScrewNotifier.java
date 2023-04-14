package main.java.cn.smallbun.screw.idea.notify;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

public class ScrewNotifier {
    private final NotificationGroup NOTIFICATION_GROUP =
            new NotificationGroup("Screw errors", NotificationDisplayType.BALLOON, true);

    public Notification notify(String content) {
        return notify(null, content);
    }

    public Notification notify(Project project, String content) {
        final Notification notification = NOTIFICATION_GROUP.createNotification(content, NotificationType.ERROR);
        notification.notify(project);
        return notification;
    }
}
