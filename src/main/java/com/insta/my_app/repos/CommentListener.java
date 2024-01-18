package com.insta.my_app.repos;

import com.insta.my_app.domain.Comment;
import com.insta.my_app.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class CommentListener extends AbstractMongoEventListener<Comment> {

    private final PrimarySequenceService primarySequenceService;

    public CommentListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Comment> event) {
        if (event.getSource().getCommentId() == null) {
            event.getSource().setCommentId((int)primarySequenceService.getNextValue());
        }
    }

}
