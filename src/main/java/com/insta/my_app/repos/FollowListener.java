package com.insta.my_app.repos;

import com.insta.my_app.domain.Follow;
import com.insta.my_app.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class FollowListener extends AbstractMongoEventListener<Follow> {

    private final PrimarySequenceService primarySequenceService;

    public FollowListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Follow> event) {
        if (event.getSource().getFollowId() == null) {
            event.getSource().setFollowId((int)primarySequenceService.getNextValue());
        }
    }

}
