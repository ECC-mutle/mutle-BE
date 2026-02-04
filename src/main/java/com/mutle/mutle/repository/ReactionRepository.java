package com.mutle.mutle.repository;

import com.mutle.mutle.entity.Bottle;
import com.mutle.mutle.entity.Reaction;
import com.mutle.mutle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    boolean existsByReactorAndBottle(User user, Bottle bottle);
}
