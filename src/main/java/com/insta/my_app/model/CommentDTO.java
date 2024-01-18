package com.insta.my_app.model;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommentDTO {

    private Integer commentId;
    private String text;
    private LocalDateTime createdAt;
    private Integer user;
    private Integer post;

}
