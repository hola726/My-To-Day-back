package com.jyp.service.my_today_service.repository;

import com.jyp.service.my_today_service.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAll(Pageable pageable);

    ///  검색 및 날짜 범위 조회
    @Query("SELECT p FROM Post p WHERE " +
            "(:keyword IS NULL OR p.content LIKE CONCAT('%', :keyword, '%')) AND " +
            "(:startDate IS NULL OR p.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR p.createdAt <= :endDate) AND " +
            "(:userId IS NULL OR p.users.id = :userId)")
    Page<Post> findByContentAndDateRange(@Param("keyword") String keyword, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate, @Param("userId") Long userId, Pageable pageable);
}
