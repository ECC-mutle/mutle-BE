package com.mutle.mutle.repository;

import com.mutle.mutle.entity.Platform;
import com.mutle.mutle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlatformRepository extends JpaRepository<Platform, Long> {
    List<Platform> findAllByUser(User user);
}
