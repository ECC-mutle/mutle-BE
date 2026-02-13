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
@Table(name = "bookmarks")
@Builder
public class Bookmark {

    @Id
    @GeneratedValue
    @Column(name = "bookmark_id")
    private Long bookmarkId;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @JoinColumn(name = "bottle_id", nullable = false)
    private Bottle bottle;

    @CreationTimestamp
    @Column(name = "bookmark_created_at", nullable = false, updatable = false)
    private Timestamp bookmarkCreatedAt;

    @Column(name = "bookmark_expires_at", nullable = false)
    private Timestamp bookmarkExpiresAt;

    @UpdateTimestamp
    @Column(name = "bookmark_updated_at", nullable = false)
    private Timestamp bookmarkUpdatedAt;

    @PrePersist
    public void calculateExpiryDate(){
        Timestamp now=Timestamp.valueOf(LocalDateTime.now());
        if (this.bookmarkCreatedAt==null){
            this.bookmarkCreatedAt = now;
        }
        if(this.bookmarkExpiresAt == null){
            this.bookmarkExpiresAt=Timestamp.valueOf(LocalDateTime.now().plusDays(7));
        }
    }
}
