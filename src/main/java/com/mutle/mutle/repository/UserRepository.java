package com.mutle.mutle.repository;

import com.mutle.mutle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String keyword);

    Optional<User> findByUserId(String userId);

    boolean existsByUserId(String userId);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
