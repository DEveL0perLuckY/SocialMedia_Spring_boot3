package com.insta.my_app.repos;

import com.insta.my_app.domain.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, Long> {

    Optional<Role> findById(Long id);
}
