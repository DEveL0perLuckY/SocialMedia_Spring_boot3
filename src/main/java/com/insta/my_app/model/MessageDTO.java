package com.insta.my_app.model;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MessageDTO {

    private Integer messageId;
    private String messageText;
    private LocalDateTime createdAt;
    private Integer sender;
    private Integer receiver;

}
