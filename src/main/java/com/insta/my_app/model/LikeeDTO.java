package com.insta.my_app.model;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LikeeDTO {

    private Integer likeId;
    private LocalDateTime createdAt;
    private Integer user;
    private Integer post;

}
