package com.insta.my_app.model;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PostDTO {

    private Integer postId;
    private String caption;
    private LocalDateTime createdAt;
    private Integer user;

}
