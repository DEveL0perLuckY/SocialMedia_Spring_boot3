package com.insta.my_app.repos;

import com.insta.my_app.domain.Notification;
import com.insta.my_app.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface NotificationRepository extends MongoRepository<Notification, Integer> {

    Notification findFirstByUser(User user);

}
