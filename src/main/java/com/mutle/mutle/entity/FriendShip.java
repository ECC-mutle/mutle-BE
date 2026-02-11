package com.mutle.mutle.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "friendships")
@Builder
public class FriendShip {

    @Id
    @GeneratedValue
    @Column(name = "friend_request_id")
    private Long friendRequestId;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User requester;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(name = "friendship_status", nullable = false, length = 20)
    private FriendshipStatus friendshipStatus;

    @CreationTimestamp
    @Column(name = "requested_at", nullable = false, updatable = false)
    private Timestamp requestedAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @Column(name = "unfriended_at")
    private Timestamp unfriendedAt = Timestamp.valueOf(LocalDateTime.now());

    // 친구 수락 시 상태 변경
    public void acceptRequest() {
        this.friendshipStatus = FriendshipStatus.ACCEPTED;
    }
}

