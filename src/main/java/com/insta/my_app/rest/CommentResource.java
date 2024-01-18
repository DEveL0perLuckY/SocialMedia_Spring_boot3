package com.insta.my_app.rest;

import com.insta.my_app.model.CommentDTO;
import com.insta.my_app.service.CommentService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/comments", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentResource {

    private final CommentService commentService;

    public CommentResource(final CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAllComments() {
        return ResponseEntity.ok(commentService.findAll());
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDTO> getComment(
            @PathVariable(name = "commentId") final Integer commentId) {
        return ResponseEntity.ok(commentService.get(commentId));
    }

    @PostMapping
    public ResponseEntity<Integer> createComment(@RequestBody @Valid final CommentDTO commentDTO) {
        final Integer createdCommentId = commentService.create(commentDTO);
        return new ResponseEntity<>(createdCommentId, HttpStatus.CREATED);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Integer> updateComment(
            @PathVariable(name = "commentId") final Integer commentId,
            @RequestBody @Valid final CommentDTO commentDTO) {
        commentService.update(commentId, commentDTO);
        return ResponseEntity.ok(commentId);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable(name = "commentId") final Integer commentId) {
        commentService.delete(commentId);
        return ResponseEntity.noContent().build();
    }

}
