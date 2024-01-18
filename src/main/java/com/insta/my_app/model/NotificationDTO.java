package com.insta.my_app.model;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NotificationDTO {

    private Integer notificationId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime isRead;
    private Integer user;

}
