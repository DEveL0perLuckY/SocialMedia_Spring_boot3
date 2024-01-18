package com.insta.my_app.repos;

import com.insta.my_app.domain.Notification;
import com.insta.my_app.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class NotificationListener extends AbstractMongoEventListener<Notification> {

    private final PrimarySequenceService primarySequenceService;

    public NotificationListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Notification> event) {
        if (event.getSource().getNotificationId() == null) {
            event.getSource().setNotificationId((int)primarySequenceService.getNextValue());
        }
    }

}
