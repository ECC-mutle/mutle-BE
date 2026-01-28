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
@Table(name = "bottles")
@Builder
public class Bottle {

    @Id
    @GeneratedValue
    @Column(name = "bottle_id")
    private Long bottleId;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private TodayQuest todayQuest;

    @ManyToOne
    @JoinColumn(name = "music_id", nullable = false)
    private Music music;

    @Column(name = "memo", length = 200)
    private String memo;

    @Column(name = "is_shared", nullable = false)
    private boolean isShared = true;

    @Column(name = "total_count", nullable = false)
    private int totalCount;

    @Column(name = "bottle_created_at", nullable = false, updatable = false)
    private Timestamp bottleCreatedAt = Timestamp.valueOf(LocalDateTime.now());

    @UpdateTimestamp
    @Column(name = "bottle_updated_at", nullable = false)
    private Timestamp bottleUpdatedAt = Timestamp.valueOf(LocalDateTime.now());

}


