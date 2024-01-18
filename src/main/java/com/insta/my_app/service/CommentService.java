package com.insta.my_app.service;

import com.insta.my_app.domain.Comment;
import com.insta.my_app.domain.Post;
import com.insta.my_app.domain.User;
import com.insta.my_app.model.CommentDTO;
import com.insta.my_app.repos.CommentRepository;
import com.insta.my_app.repos.PostRepository;
import com.insta.my_app.repos.UserRepository;
import com.insta.my_app.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentService(final CommentRepository commentRepository,
            final UserRepository userRepository, final PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public List<CommentDTO> findAll() {
        final List<Comment> comments = commentRepository.findAll(Sort.by("commentId"));
        return comments.stream()
                .map(comment -> mapToDTO(comment, new CommentDTO()))
                .toList();
    }

    public CommentDTO get(final Integer commentId) {
        return commentRepository.findById(commentId)
                .map(comment -> mapToDTO(comment, new CommentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final CommentDTO commentDTO) {
        final Comment comment = new Comment();
        mapToEntity(commentDTO, comment);
        return commentRepository.save(comment).getCommentId();
    }

    public void update(final Integer commentId, final CommentDTO commentDTO) {
        final Comment comment = commentRepository.findById(commentId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(commentDTO, comment);
        commentRepository.save(comment);
    }

    public void delete(final Integer commentId) {
        commentRepository.deleteById(commentId);
    }

    private CommentDTO mapToDTO(final Comment comment, final CommentDTO commentDTO) {
        commentDTO.setCommentId(comment.getCommentId());
        commentDTO.setText(comment.getText());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setUser(comment.getUser() == null ? null : comment.getUser().getUserId());
        commentDTO.setPost(comment.getPost() == null ? null : comment.getPost().getPostId());
        return commentDTO;
    }

    private Comment mapToEntity(final CommentDTO commentDTO, final Comment comment) {
        comment.setText(commentDTO.getText());
        comment.setCreatedAt(commentDTO.getCreatedAt());
        final User user = commentDTO.getUser() == null ? null : userRepository.findById(commentDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        comment.setUser(user);
        final Post post = commentDTO.getPost() == null ? null : postRepository.findById(commentDTO.getPost())
                .orElseThrow(() -> new NotFoundException("post not found"));
        comment.setPost(post);
        return comment;
    }

}
