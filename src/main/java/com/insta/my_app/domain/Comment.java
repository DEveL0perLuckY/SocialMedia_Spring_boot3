package com.insta.my_app.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


@Document("comments")
@Getter
@Setter
public class Comment {

    @Id
    private Integer commentId;

    private String text;

    private LocalDateTime createdAt;

    @DocumentReference(lazy = true)
    private User user;

    @DocumentReference(lazy = true)
    private Post post;

}
