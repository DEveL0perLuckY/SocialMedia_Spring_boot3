package com.insta.my_app.rest;

import com.insta.my_app.model.LikeeDTO;
import com.insta.my_app.service.LikeeService;
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
@RequestMapping(value = "/api/likees", produces = MediaType.APPLICATION_JSON_VALUE)
public class LikeeResource {

    private final LikeeService likeeService;

    public LikeeResource(final LikeeService likeeService) {
        this.likeeService = likeeService;
    }

    @GetMapping
    public ResponseEntity<List<LikeeDTO>> getAllLikees() {
        return ResponseEntity.ok(likeeService.findAll());
    }

    @GetMapping("/{likeId}")
    public ResponseEntity<LikeeDTO> getLikee(@PathVariable(name = "likeId") final Integer likeId) {
        return ResponseEntity.ok(likeeService.get(likeId));
    }

    @PostMapping
    public ResponseEntity<Integer> createLikee(@RequestBody @Valid final LikeeDTO likeeDTO) {
        final Integer createdLikeId = likeeService.create(likeeDTO);
        return new ResponseEntity<>(createdLikeId, HttpStatus.CREATED);
    }

    @PutMapping("/{likeId}")
    public ResponseEntity<Integer> updateLikee(@PathVariable(name = "likeId") final Integer likeId,
            @RequestBody @Valid final LikeeDTO likeeDTO) {
        likeeService.update(likeId, likeeDTO);
        return ResponseEntity.ok(likeId);
    }

    @DeleteMapping("/{likeId}")
    public ResponseEntity<Void> deleteLikee(@PathVariable(name = "likeId") final Integer likeId) {
        likeeService.delete(likeId);
        return ResponseEntity.noContent().build();
    }

}
