package com.insta.my_app.service;

import com.insta.my_app.domain.Comment;
import com.insta.my_app.domain.Likee;
import com.insta.my_app.domain.Post;
import com.insta.my_app.domain.User;
import com.insta.my_app.model.PostDTO;
import com.insta.my_app.repos.CommentRepository;
import com.insta.my_app.repos.LikeeRepository;
import com.insta.my_app.repos.PostRepository;
import com.insta.my_app.repos.UserRepository;
import com.insta.my_app.util.NotFoundException;
import com.insta.my_app.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeeRepository likeeRepository;

    public PostService(final PostRepository postRepository, final UserRepository userRepository,
            final CommentRepository commentRepository, final LikeeRepository likeeRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.likeeRepository = likeeRepository;
    }

    public List<PostDTO> findAll() {
        final List<Post> posts = postRepository.findAll(Sort.by("postId"));
        return posts.stream()
                .map(post -> mapToDTO(post, new PostDTO()))
                .toList();
    }

    public PostDTO get(final Integer postId) {
        return postRepository.findById(postId)
                .map(post -> mapToDTO(post, new PostDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final PostDTO postDTO) {
        final Post post = new Post();
        mapToEntity(postDTO, post);
        return postRepository.save(post).getPostId();
    }

    public void update(final Integer postId, final PostDTO postDTO) {
        final Post post = postRepository.findById(postId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(postDTO, post);
        postRepository.save(post);
    }

    public void delete(final Integer postId) {
        postRepository.deleteById(postId);
    }

    private PostDTO mapToDTO(final Post post, final PostDTO postDTO) {
        postDTO.setPostId(post.getPostId());
        postDTO.setCaption(post.getCaption());
        postDTO.setCreatedAt(post.getCreatedAt());
        postDTO.setUser(post.getUser() == null ? null : post.getUser().getUserId());
        return postDTO;
    }

    private Post mapToEntity(final PostDTO postDTO, final Post post) {
        post.setCaption(postDTO.getCaption());
        post.setCreatedAt(postDTO.getCreatedAt());
        final User user = postDTO.getUser() == null ? null : userRepository.findById(postDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        post.setUser(user);
        return post;
    }

    public String getReferencedWarning(final Integer postId) {
        final Post post = postRepository.findById(postId)
                .orElseThrow(NotFoundException::new);
        final Comment postComment = commentRepository.findFirstByPost(post);
        if (postComment != null) {
            return WebUtils.getMessage("post.comment.post.referenced", postComment.getCommentId());
        }
        final Likee postLikee = likeeRepository.findFirstByPost(post);
        if (postLikee != null) {
            return WebUtils.getMessage("post.likee.post.referenced", postLikee.getLikeId());
        }
        return null;
    }

}
