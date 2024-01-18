package com.insta.my_app.repos;

import com.insta.my_app.domain.Post;
import com.insta.my_app.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class PostListener extends AbstractMongoEventListener<Post> {

    private final PrimarySequenceService primarySequenceService;

    public PostListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Post> event) {
        if (event.getSource().getPostId() == null) {
            event.getSource().setPostId((int)primarySequenceService.getNextValue());
        }
    }

}
