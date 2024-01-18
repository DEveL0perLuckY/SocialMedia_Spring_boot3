package com.insta.my_app.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


@Document("users")
@Getter
@Setter
public class User {

    @Id
    private Integer userId;

    @NotNull
    @Size(max = 255)
    private String username;

    @NotNull
    @Size(max = 255)
    private String email;

    @NotNull
    @Size(max = 255)
    private String password;

    @Size(max = 255)
    private String fullName;

    private String bio;

    private LocalDateTime createdAt;

    @DocumentReference(lazy = true, lookup = "{ 'user' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Post> userPosts;

    @DocumentReference(lazy = true, lookup = "{ 'user' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Comment> userComments;

    @DocumentReference(lazy = true, lookup = "{ 'user' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Likee> userLikes;

    @DocumentReference(lazy = true, lookup = "{ 'follower' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Follow> followerFollows;

    @DocumentReference(lazy = true, lookup = "{ 'following' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Follow> followingFollows;

    @DocumentReference(lazy = true, lookup = "{ 'sender' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Message> senderMessages;

    @DocumentReference(lazy = true, lookup = "{ 'receiver' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Message> receiverMessages;

    @DocumentReference(lazy = true, lookup = "{ 'user' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Notification> userNotifications;

    @DocumentReference(lazy = true)
    private Set<Role> roleId;

}
