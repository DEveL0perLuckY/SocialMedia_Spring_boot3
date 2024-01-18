package com.insta.my_app.repos;

import com.insta.my_app.domain.Message;
import com.insta.my_app.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface MessageRepository extends MongoRepository<Message, Integer> {

    Message findFirstBySender(User user);

    Message findFirstByReceiver(User user);

}
