package com.insta.my_app.repos;

import com.insta.my_app.domain.Follow;
import com.insta.my_app.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface FollowRepository extends MongoRepository<Follow, Integer> {

    Follow findFirstByFollower(User user);

    Follow findFirstByFollowing(User user);

}
