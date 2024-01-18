package com.insta.my_app.repos;

import com.insta.my_app.domain.Message;
import com.insta.my_app.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class MessageListener extends AbstractMongoEventListener<Message> {

    private final PrimarySequenceService primarySequenceService;

    public MessageListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Message> event) {
        if (event.getSource().getMessageId() == null) {
            event.getSource().setMessageId((int)primarySequenceService.getNextValue());
        }
    }

}
