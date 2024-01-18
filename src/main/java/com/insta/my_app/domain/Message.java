package com.insta.my_app.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


@Document("messages")
@Getter
@Setter
public class Message {

    @Id
    private Integer messageId;

    private String messageText;

    private LocalDateTime createdAt;

    @DocumentReference(lazy = true)
    private User sender;

    @DocumentReference(lazy = true)
    private User receiver;

}
