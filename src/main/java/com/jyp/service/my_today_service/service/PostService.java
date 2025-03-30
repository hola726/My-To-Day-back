package com.jyp.service.my_today_service.service;

import com.jyp.service.my_today_service.dto.PageResponseDto;
import com.jyp.service.my_today_service.dto.PostResponseDto;
import com.jyp.service.my_today_service.model.Post;
import com.jyp.service.my_today_service.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public PageResponseDto<PostResponseDto> getPosts(Pageable pageable) {
        Page<Post> postPage = postRepository.findAll(pageable);
        List<PostResponseDto> postResponseDtos = postPage.getContent().stream().map(e -> new PostResponseDto(e.getId(), e.getImagePath(), e.getContent(), e.getLatitude(), e.getLongitude(), e.getUsers().getId())).toList();
        return new PageResponseDto<>(postResponseDtos, postPage.getPageable().getPageNumber(), postPage.getPageable().getPageSize(), postPage.getTotalElements(), postPage.getTotalPages(), postPage.isLast());
    }

}
