package com.insta.my_app.rest;

import com.insta.my_app.model.FollowDTO;
import com.insta.my_app.service.FollowService;
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
@RequestMapping(value = "/api/follows", produces = MediaType.APPLICATION_JSON_VALUE)
public class FollowResource {

    private final FollowService followService;

    public FollowResource(final FollowService followService) {
        this.followService = followService;
    }

    @GetMapping
    public ResponseEntity<List<FollowDTO>> getAllFollows() {
        return ResponseEntity.ok(followService.findAll());
    }

    @GetMapping("/{followId}")
    public ResponseEntity<FollowDTO> getFollow(
            @PathVariable(name = "followId") final Integer followId) {
        return ResponseEntity.ok(followService.get(followId));
    }

    @PostMapping
    public ResponseEntity<Integer> createFollow(@RequestBody @Valid final FollowDTO followDTO) {
        final Integer createdFollowId = followService.create(followDTO);
        return new ResponseEntity<>(createdFollowId, HttpStatus.CREATED);
    }

    @PutMapping("/{followId}")
    public ResponseEntity<Integer> updateFollow(
            @PathVariable(name = "followId") final Integer followId,
            @RequestBody @Valid final FollowDTO followDTO) {
        followService.update(followId, followDTO);
        return ResponseEntity.ok(followId);
    }

    @DeleteMapping("/{followId}")
    public ResponseEntity<Void> deleteFollow(
            @PathVariable(name = "followId") final Integer followId) {
        followService.delete(followId);
        return ResponseEntity.noContent().build();
    }

}
