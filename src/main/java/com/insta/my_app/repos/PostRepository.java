package com.insta.my_app.repos;

import com.insta.my_app.domain.Post;
import com.insta.my_app.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface PostRepository extends MongoRepository<Post, Integer> {

    Post findFirstByUser(User user);

}
