package com.mutle.mutle.repository;

import com.mutle.mutle.entity.FriendShip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FriendShipRepository extends JpaRepository<FriendShip, Long> {

    @Query("SELECT f FROM FriendShip f WHERE " +
            "(f.requester.id = :meId AND f.receiver.id = :youId) OR " +
            "(f.requester.id = :youId AND f.receiver.id = :meId)")
    Optional<FriendShip> findRelation(Long meId, Long youId);
}
