package com.insta.my_app.service;

import com.insta.my_app.domain.Likee;
import com.insta.my_app.domain.Post;
import com.insta.my_app.domain.User;
import com.insta.my_app.model.LikeeDTO;
import com.insta.my_app.repos.LikeeRepository;
import com.insta.my_app.repos.PostRepository;
import com.insta.my_app.repos.UserRepository;
import com.insta.my_app.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class LikeeService {

    private final LikeeRepository likeeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public LikeeService(final LikeeRepository likeeRepository, final UserRepository userRepository,
            final PostRepository postRepository) {
        this.likeeRepository = likeeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public List<LikeeDTO> findAll() {
        final List<Likee> likees = likeeRepository.findAll(Sort.by("likeId"));
        return likees.stream()
                .map(likee -> mapToDTO(likee, new LikeeDTO()))
                .toList();
    }

    public LikeeDTO get(final Integer likeId) {
        return likeeRepository.findById(likeId)
                .map(likee -> mapToDTO(likee, new LikeeDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final LikeeDTO likeeDTO) {
        final Likee likee = new Likee();
        mapToEntity(likeeDTO, likee);
        return likeeRepository.save(likee).getLikeId();
    }

    public void update(final Integer likeId, final LikeeDTO likeeDTO) {
        final Likee likee = likeeRepository.findById(likeId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(likeeDTO, likee);
        likeeRepository.save(likee);
    }

    public void delete(final Integer likeId) {
        likeeRepository.deleteById(likeId);
    }

    private LikeeDTO mapToDTO(final Likee likee, final LikeeDTO likeeDTO) {
        likeeDTO.setLikeId(likee.getLikeId());
        likeeDTO.setCreatedAt(likee.getCreatedAt());
        likeeDTO.setUser(likee.getUser() == null ? null : likee.getUser().getUserId());
        likeeDTO.setPost(likee.getPost() == null ? null : likee.getPost().getPostId());
        return likeeDTO;
    }

    private Likee mapToEntity(final LikeeDTO likeeDTO, final Likee likee) {
        likee.setCreatedAt(likeeDTO.getCreatedAt());
        final User user = likeeDTO.getUser() == null ? null : userRepository.findById(likeeDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        likee.setUser(user);
        final Post post = likeeDTO.getPost() == null ? null : postRepository.findById(likeeDTO.getPost())
                .orElseThrow(() -> new NotFoundException("post not found"));
        likee.setPost(post);
        return likee;
    }

}
