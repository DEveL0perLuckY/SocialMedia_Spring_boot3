package com.insta.my_app.repos;

import com.insta.my_app.domain.Likee;
import com.insta.my_app.domain.Post;
import com.insta.my_app.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface LikeeRepository extends MongoRepository<Likee, Integer> {

    Likee findFirstByUser(User user);

    Likee findFirstByPost(Post post);

}
