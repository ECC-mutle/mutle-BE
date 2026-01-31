package com.mutle.mutle.repository;

import com.mutle.mutle.entity.Bookmark;
import com.mutle.mutle.entity.Bottle;
import com.mutle.mutle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    boolean existsByUserAndBottle(User user, Bottle bottle);

    @Query("SELECT b FROM Bookmark b WHERE b.user.id = :userId AND b.bookmarkCreatedAt > :sevenDaysAgo")
    List<Bookmark> findActiveBookmarks(@Param("userId") Long userId, @Param("sevenDaysAgo") Timestamp sevenDaysAgo);

}
