package com.insta.my_app.repos;

import com.insta.my_app.domain.Comment;
import com.insta.my_app.domain.Post;
import com.insta.my_app.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CommentRepository extends MongoRepository<Comment, Integer> {

    Comment findFirstByUser(User user);

    Comment findFirstByPost(Post post);

}
