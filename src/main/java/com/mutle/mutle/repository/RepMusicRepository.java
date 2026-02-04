package com.mutle.mutle.repository;

import com.mutle.mutle.entity.RepMusic;
import com.mutle.mutle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepMusicRepository extends JpaRepository<RepMusic, Long> {
    Optional<RepMusic> findByUser(User user);

}
