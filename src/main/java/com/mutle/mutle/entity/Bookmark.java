package com.mutle.mutle.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "bookmarks")
@Builder
public class Bookmark {

    @Id
    @GeneratedValue
    @Column(name = "bookmark_id")
    private Long bookmarkId;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "bottle_id", nullable = false)
    private Bottle bottle;

    @Column(name = "bookmark_created_at", nullable = false, updatable = false)
    private Timestamp bookmarkCreatedAt = Timestamp.valueOf(LocalDateTime.now());

    @Column(name = "bookmark_expires_at", nullable = false)
    private Timestamp bookmarkExpiresAt = Timestamp.valueOf(LocalDateTime.now().plusDays(7));

    @UpdateTimestamp
    @Column(name = "bookmark_updated_at", nullable = false)
    private Timestamp bookmarkUpdatedAt= Timestamp.valueOf(LocalDateTime.now());
}
