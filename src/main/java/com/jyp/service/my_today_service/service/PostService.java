package com.jyp.service.my_today_service.service;

import com.jyp.service.my_today_service.model.Post;
import com.jyp.service.my_today_service.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public Optional<Post> findPostById(Long id) {
        return postRepository.findById(id);
    }

    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }

}
