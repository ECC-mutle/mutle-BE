package com.mutle.mutle.entity;

import jakarta.persistence.*;
import lombok.*;
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
@Table(name = "reactions")
@Builder
public class Reaction {

    @Id
    @GeneratedValue
    @Column(name = "reaction_id")
    private Long reactionId;

    @ManyToOne
    @JoinColumn(name = "bottle_id", nullable = false)
    private Bottle bottle;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User reactor;

    @Column(name = "reaction_created_at", nullable = false, updatable = false)
    private Timestamp reactionCreatedAt = Timestamp.valueOf(LocalDateTime.now());

    @UpdateTimestamp
    @Column(name = "reaction_updated_at", nullable = false)
    private Timestamp reactionUpdatedAt = Timestamp.valueOf(LocalDateTime.now());
}
