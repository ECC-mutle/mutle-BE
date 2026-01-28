package com.mutle.mutle.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Table(name = "rep_musics")
@NoArgsConstructor

@AllArgsConstructor
@Builder

public class RepMusic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rep_music_id")
    private Long repMusicId;

    @CreationTimestamp
    @Column(name="rep_music_created_at", nullable = false, updatable = false)
    private Timestamp repMusicCreatedAt;

    @UpdateTimestamp
    @Column(name="rep_music_updated_at", nullable = false)
    private Timestamp repMusicUpdatedAt;

    @OneToOne
    @JoinColumn(name="id")
    User user;

    @OneToOne
    @JoinColumn(name="music_id")
    Music music;
}
