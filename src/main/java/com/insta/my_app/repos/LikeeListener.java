package com.insta.my_app.repos;

import com.insta.my_app.domain.Likee;
import com.insta.my_app.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class LikeeListener extends AbstractMongoEventListener<Likee> {

    private final PrimarySequenceService primarySequenceService;

    public LikeeListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Likee> event) {
        if (event.getSource().getLikeId() == null) {
            event.getSource().setLikeId((int)primarySequenceService.getNextValue());
        }
    }

}
