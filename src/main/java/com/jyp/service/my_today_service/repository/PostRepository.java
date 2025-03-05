package com.jyp.service.my_today_service.repository;

import com.jyp.service.my_today_service.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
