package com.insta.my_app.service;

import com.insta.my_app.domain.Comment;
import com.insta.my_app.domain.Follow;
import com.insta.my_app.domain.Likee;
import com.insta.my_app.domain.Message;
import com.insta.my_app.domain.Notification;
import com.insta.my_app.domain.Post;
import com.insta.my_app.domain.Role;
import com.insta.my_app.domain.User;
import com.insta.my_app.model.UserDTO;
import com.insta.my_app.repos.CommentRepository;
import com.insta.my_app.repos.FollowRepository;
import com.insta.my_app.repos.LikeeRepository;
import com.insta.my_app.repos.MessageRepository;
import com.insta.my_app.repos.NotificationRepository;
import com.insta.my_app.repos.PostRepository;
import com.insta.my_app.repos.RoleRepository;
import com.insta.my_app.repos.UserRepository;
import com.insta.my_app.util.NotFoundException;
import com.insta.my_app.util.WebUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeeRepository likeeRepository;
    private final FollowRepository followRepository;
    private final MessageRepository messageRepository;
    private final NotificationRepository notificationRepository;

    public UserService(final UserRepository userRepository, final RoleRepository roleRepository,
            final PostRepository postRepository, final CommentRepository commentRepository,
            final LikeeRepository likeeRepository, final FollowRepository followRepository,
            final MessageRepository messageRepository,
            final NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.likeeRepository = likeeRepository;
        this.followRepository = followRepository;
        this.messageRepository = messageRepository;
        this.notificationRepository = notificationRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("userId"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Integer userId) {
        return userRepository.findById(userId)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getUserId();
    }

    public void update(final Integer userId, final UserDTO userDTO) {
        final User user = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Integer userId) {
        userRepository.deleteById(userId);
    }

    public UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setFullName(user.getFullName());
        userDTO.setBio(user.getBio());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setRoleId(user.getRoleId().stream()
                .map(role -> role.getId())
                .toList());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setFullName(userDTO.getFullName());
        user.setBio(userDTO.getBio());
        user.setCreatedAt(userDTO.getCreatedAt());
        final List<Role> roleId = iterableToList(roleRepository.findAllById(
                userDTO.getRoleId() == null ? Collections.emptyList() : userDTO.getRoleId()));
        if (roleId.size() != (userDTO.getRoleId() == null ? 0 : userDTO.getRoleId().size())) {
            throw new NotFoundException("one of roleId not found");
        }
        user.setRoleId(roleId.stream().collect(Collectors.toSet()));
        return user;
    }

    private <T> List<T> iterableToList(final Iterable<T> iterable) {
        final List<T> list = new ArrayList<T>();
        iterable.forEach(item -> list.add(item));
        return list;
    }

    public String getReferencedWarning(final Integer userId) {
        final User user = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new);
        final Post userPost = postRepository.findFirstByUser(user);
        if (userPost != null) {
            return WebUtils.getMessage("user.post.user.referenced", userPost.getPostId());
        }
        final Comment userComment = commentRepository.findFirstByUser(user);
        if (userComment != null) {
            return WebUtils.getMessage("user.comment.user.referenced", userComment.getCommentId());
        }
        final Likee userLikee = likeeRepository.findFirstByUser(user);
        if (userLikee != null) {
            return WebUtils.getMessage("user.likee.user.referenced", userLikee.getLikeId());
        }
        final Follow followerFollow = followRepository.findFirstByFollower(user);
        if (followerFollow != null) {
            return WebUtils.getMessage("user.follow.follower.referenced", followerFollow.getFollowId());
        }
        final Follow followingFollow = followRepository.findFirstByFollowing(user);
        if (followingFollow != null) {
            return WebUtils.getMessage("user.follow.following.referenced", followingFollow.getFollowId());
        }
        final Message senderMessage = messageRepository.findFirstBySender(user);
        if (senderMessage != null) {
            return WebUtils.getMessage("user.message.sender.referenced", senderMessage.getMessageId());
        }
        final Message receiverMessage = messageRepository.findFirstByReceiver(user);
        if (receiverMessage != null) {
            return WebUtils.getMessage("user.message.receiver.referenced", receiverMessage.getMessageId());
        }
        final Notification userNotification = notificationRepository.findFirstByUser(user);
        if (userNotification != null) {
            return WebUtils.getMessage("user.notification.user.referenced", userNotification.getNotificationId());
        }
        return null;
    }

}
