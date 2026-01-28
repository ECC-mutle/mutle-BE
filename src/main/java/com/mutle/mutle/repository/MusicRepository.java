package com.mutle.mutle.repository;

import com.mutle.mutle.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music, Long> {
}
