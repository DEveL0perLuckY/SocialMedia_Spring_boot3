package com.insta.my_app.model;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FollowDTO {

    private Integer followId;
    private LocalDateTime createdAt;
    private Integer follower;
    private Integer following;

}
