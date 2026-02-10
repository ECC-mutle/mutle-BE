package com.mutle.mutle.repository;

import com.mutle.mutle.entity.TodayQuest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface TodayQuestRepository extends JpaRepository<TodayQuest, Long> {

    Optional<TodayQuest> findByQuestionId(Long questionId);

    @Query("select t from TodayQuest t " +
            "where t.date >= :start " +
            "and t.date < :end")
    Optional<TodayQuest> findByDate(@Param("start") Date start,
                                    @Param("end") Date end);
}
