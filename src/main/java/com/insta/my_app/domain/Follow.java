package com.insta.my_app.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


@Document("follows")
@Getter
@Setter
public class Follow {

    @Id
    private Integer followId;

    private LocalDateTime createdAt;

    @DocumentReference(lazy = true)
    private User follower;

    @DocumentReference(lazy = true)
    private User following;

}
