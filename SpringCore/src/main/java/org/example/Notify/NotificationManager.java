package org.example.Notify;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationManager {

    private final List<Notifier> notifiers;

    public NotificationManager(List<Notifier> notifiers) {
        this.notifiers = notifiers;
    }

    public void notifyAll(String message) {

        for (Notifier notifier : notifiers) {
            notifier.send(message);
        }
    }
}