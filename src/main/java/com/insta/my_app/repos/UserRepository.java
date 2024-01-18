package com.insta.my_app.repos;

import com.insta.my_app.domain.Role;
import com.insta.my_app.domain.User;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.query.FluentQuery;

@EnableMongoRepositories
public interface UserRepository extends MongoRepository<User, Integer> {

    User findFirstByRoleId(Role role);

    Optional<User> findByEmail(String email);


    List<User> findAllByRoleId(Role role);

}
