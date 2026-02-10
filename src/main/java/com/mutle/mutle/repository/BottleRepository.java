package com.mutle.mutle.repository;

import com.mutle.mutle.entity.Bottle;
import com.mutle.mutle.entity.TodayQuest;
import com.mutle.mutle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BottleRepository extends JpaRepository<Bottle, Long> {

    Optional<Bottle> findByBottleId(Long bottleId);

    //랜덤 유리병 조회
    @Query(
            value = "SELECT * FROM bottles b WHERE b.id != :id ORDER BY RANDOM() LIMIT 1",
            nativeQuery = true
    )
    Optional<Bottle> findRandomBottle(@Param("id") Long id);

    List<Bottle> bottleId(Long bottleId);

    boolean existsByBottleIdAndBottleCreatedAtAfter(Long BottleId, LocalDateTime dateTime);
    @Query("SELECT b FROM Bottle b WHERE b.user = :user " +
            "AND b.bottleCreatedAt >= :start " +
            "AND b.bottleCreatedAt < :end")
    List<Bottle> findByUserAndYearAndMonth(
            @Param("user") User user,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
