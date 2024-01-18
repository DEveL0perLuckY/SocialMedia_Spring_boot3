package com.insta.my_app.service;

import com.insta.my_app.domain.Follow;
import com.insta.my_app.domain.User;
import com.insta.my_app.model.FollowDTO;
import com.insta.my_app.repos.FollowRepository;
import com.insta.my_app.repos.UserRepository;
import com.insta.my_app.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowService(final FollowRepository followRepository,
            final UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    public List<FollowDTO> findAll() {
        final List<Follow> follows = followRepository.findAll(Sort.by("followId"));
        return follows.stream()
                .map(follow -> mapToDTO(follow, new FollowDTO()))
                .toList();
    }

    public FollowDTO get(final Integer followId) {
        return followRepository.findById(followId)
                .map(follow -> mapToDTO(follow, new FollowDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final FollowDTO followDTO) {
        final Follow follow = new Follow();
        mapToEntity(followDTO, follow);
        return followRepository.save(follow).getFollowId();
    }

    public void update(final Integer followId, final FollowDTO followDTO) {
        final Follow follow = followRepository.findById(followId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(followDTO, follow);
        followRepository.save(follow);
    }

    public void delete(final Integer followId) {
        followRepository.deleteById(followId);
    }

    private FollowDTO mapToDTO(final Follow follow, final FollowDTO followDTO) {
        followDTO.setFollowId(follow.getFollowId());
        followDTO.setCreatedAt(follow.getCreatedAt());
        followDTO.setFollower(follow.getFollower() == null ? null : follow.getFollower().getUserId());
        followDTO.setFollowing(follow.getFollowing() == null ? null : follow.getFollowing().getUserId());
        return followDTO;
    }

    private Follow mapToEntity(final FollowDTO followDTO, final Follow follow) {
        follow.setCreatedAt(followDTO.getCreatedAt());
        final User follower = followDTO.getFollower() == null ? null : userRepository.findById(followDTO.getFollower())
                .orElseThrow(() -> new NotFoundException("follower not found"));
        follow.setFollower(follower);
        final User following = followDTO.getFollowing() == null ? null : userRepository.findById(followDTO.getFollowing())
                .orElseThrow(() -> new NotFoundException("following not found"));
        follow.setFollowing(following);
        return follow;
    }

}
