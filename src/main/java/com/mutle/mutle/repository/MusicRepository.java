package com.mutle.mutle.repository;

import com.mutle.mutle.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MusicRepository extends JpaRepository<Music, Long> {

    Optional<Music> findByMusicId(Long musicId);


    Optional<Music> findByFirstByTrackNameAndArtistName(String trackName, String artistName);
}
