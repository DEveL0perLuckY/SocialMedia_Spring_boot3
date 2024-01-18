package com.insta.my_app.domain;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


@Document("posts")
@Getter
@Setter
public class Post {

    @Id
    private Integer postId;

    private String caption;

    private LocalDateTime createdAt;

    @DocumentReference(lazy = true)
    private User user;

    @DocumentReference(lazy = true, lookup = "{ 'post' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Comment> postComments;

    @DocumentReference(lazy = true, lookup = "{ 'post' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Likee> postLikes;

}
