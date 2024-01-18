package com.insta.my_app.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


@Document("notifications")
@Getter
@Setter
public class Notification {

    @Id
    private Integer notificationId;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime isRead;

    @DocumentReference(lazy = true)
    private User user;

}
