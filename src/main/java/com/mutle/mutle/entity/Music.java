package com.mutle.mutle.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "musics")
@Builder
public class Music {

    @Id
    @GeneratedValue
    @Column(name = "music_id")
    private Long MusicId;

    @Column(name = "track_name", nullable = false, length = 200)
    private String trackName;

    @Column(name = "artist_name", nullable = false, length = 200)
    private String artistName;

    @Column(name = "album_cover_url", nullable = false, length = 500)
    private String albumCoverUrl;

    @Column(name = "music_created_at", nullable = false, updatable = false)
    private Timestamp musicCreatedAt;

    @UpdateTimestamp
    @Column(name = "music_created_at", nullable = false)
    private Timestamp musicUpdatedAt;
}
