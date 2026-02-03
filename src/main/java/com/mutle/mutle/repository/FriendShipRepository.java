package com.mutle.mutle.repository;

import com.mutle.mutle.entity.FriendShip;
import com.mutle.mutle.entity.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendShipRepository extends JpaRepository<FriendShip, Long> {

    @Query("SELECT f FROM FriendShip f WHERE " +
            "(f.requester.id = :id AND f.receiver.id = :targetId) OR " +
            "(f.requester.id = :targetId AND f.receiver.id = :id)")
    Optional<FriendShip> findRelation(Long id, Long youId);

    List<FriendShip> findByReceiverIdAndFriendshipStatus(Long receiverId, FriendshipStatus requestSent);

    @Query("SELECT f FROM FriendShip f WHERE " +
            "(f.requester.id = :userId OR f.receiver.id = :userId) " +
            "AND f.friendshipStatus = com.mutle.mutle.entity.FriendshipStatus.ACCEPTED")
    List<FriendShip> findByAcceptedFriends(@Param("userId") Long id);
}
