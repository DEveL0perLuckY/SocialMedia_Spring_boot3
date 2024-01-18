package com.insta.my_app.rest;

import com.insta.my_app.model.PostDTO;
import com.insta.my_app.service.PostService;
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
@RequestMapping(value = "/api/posts", produces = MediaType.APPLICATION_JSON_VALUE)
public class PostResource {

    private final PostService postService;

    public PostResource(final PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        return ResponseEntity.ok(postService.findAll());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable(name = "postId") final Integer postId) {
        return ResponseEntity.ok(postService.get(postId));
    }

    @PostMapping
    public ResponseEntity<Integer> createPost(@RequestBody @Valid final PostDTO postDTO) {
        final Integer createdPostId = postService.create(postDTO);
        return new ResponseEntity<>(createdPostId, HttpStatus.CREATED);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Integer> updatePost(@PathVariable(name = "postId") final Integer postId,
            @RequestBody @Valid final PostDTO postDTO) {
        postService.update(postId, postDTO);
        return ResponseEntity.ok(postId);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable(name = "postId") final Integer postId) {
        postService.delete(postId);
        return ResponseEntity.noContent().build();
    }

}
