package com.jyp.service.my_today_service.controller;

import com.jyp.service.my_today_service.dto.ApiResponse;
import com.jyp.service.my_today_service.dto.PageResponseDto;
import com.jyp.service.my_today_service.dto.PostRequestDto;
import com.jyp.service.my_today_service.dto.PostResponseDto;
import com.jyp.service.my_today_service.model.Post;
import com.jyp.service.my_today_service.model.Users;
import com.jyp.service.my_today_service.security.AuthUtil;
import com.jyp.service.my_today_service.service.PostService;
import com.jyp.service.my_today_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;

@Tag(name = "게시물 API")
@RestController
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final AuthUtil authUtil;

    @Autowired
    public PostController(PostService postService, UserService userService, AuthUtil authUtil) {
        this.postService = postService;
        this.userService = userService;
        this.authUtil = authUtil;
    }


    @Operation(summary = "게시물 조회")
    @GetMapping("v1.0/post")
    public ResponseEntity<ApiResponse<PageResponseDto<PostResponseDto>>> getPost(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Instant startDate,
            @RequestParam(required = false) Instant endDate,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        final Users user = authUtil.getUser();
        PageResponseDto<PostResponseDto> pageResponseDto = postService.getPosts(keyword, startDate, endDate, user.getId(), pageable);
        final ApiResponse<PageResponseDto<PostResponseDto>> response = new ApiResponse<>(true, "게시물 목록 조회 성공", "", pageResponseDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시물 등록")
    @Transactional
    @PostMapping("v1.0/post")
    public ResponseEntity<ApiResponse<PostResponseDto>> setPost(@RequestBody @Valid PostRequestDto postRequestDto) {
        final Users user = authUtil.getUser();
        final Post post = new Post(
                postRequestDto.getImagePath(),
                postRequestDto.getContent(),
                postRequestDto.getLatitude(),
                postRequestDto.getLongitude()
        );
        post.setUsers(user);
        final Post savePost = postService.savePost(post);

        PostResponseDto responseDto = new PostResponseDto(
                savePost.getId(),
                savePost.getImagePath(),
                savePost.getContent(),
                savePost.getLatitude(),
                savePost.getLongitude()
                , savePost.getUsers().getId()
        );
        ApiResponse<PostResponseDto> response = new ApiResponse<>(true, "게시물이 성공적으로 등록되었습니다.", "", responseDto);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @Operation(summary = "게시물 수정")
    @Transactional
    @PutMapping("v1.0/post")
    public ResponseEntity<ApiResponse<PostResponseDto>> updatePost(@RequestBody @Valid PostRequestDto postRequestDto, @RequestParam Long postId) {
        if (postRequestDto.isEmpty()) {
            throw new RuntimeException("수정할 내용이 없습니다.");
        }

        final Optional<Post> post = postService.findPostById(postId);
        if (post.isEmpty()) {
            throw new RuntimeException("게시물을 찾을 수 없습니다.");
        }
        final Users user = authUtil.getUser();
        final Users postUser = post.get().getUsers();
        if (!user.getId().equals(postUser.getId())) {
            throw new RuntimeException("게시물 수정 권한이 없습니다.(다른사용자의 게시물)");
        }

        if (postRequestDto.getImagePath().equals(post.get().getImagePath()) && postRequestDto.getContent().equals(post.get().getContent()) && postRequestDto.getLatitude().equals(post.get().getLatitude()) && postRequestDto.getLongitude().equals(post.get().getLongitude())) {
            throw new RuntimeException("수정할 내용이 없습니다.");
        }

        post.get().setImagePath(postRequestDto.getImagePath());
        post.get().setContent(postRequestDto.getContent());
        post.get().setLatitude(postRequestDto.getLatitude());
        post.get().setLongitude(postRequestDto.getLongitude());
        post.get().setUpdatedAt(Instant.now());

        final Post savePost = postService.savePost(post.get());
        final PostResponseDto responseDto = new PostResponseDto(
                savePost.getId(),
                savePost.getImagePath(),
                savePost.getContent(),
                savePost.getLatitude(),
                savePost.getLongitude(),
                savePost.getUsers().getId()
        );

        ApiResponse<PostResponseDto> response = new ApiResponse<>(true, "게시물이 성공적으로 수정되었습니다.", "", responseDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "게시물 삭제")
    @DeleteMapping("v1.0/post")
    public ResponseEntity<ApiResponse<PostResponseDto>> deletePost(@RequestParam Long postId) {
        final Optional<Post> post = postService.findPostById(postId);

        if (post.isEmpty()) {
            throw new RuntimeException("게시물을 찾을 수 없습니다.");
        }
        postService.deletePostById(postId);
        final PostResponseDto responseDto = new PostResponseDto(
                post.get().getId(),
                post.get().getImagePath(), post.get().getContent(), post.get().getLatitude(), post.get().getLongitude(), post.get().getUsers().getId());
        final ApiResponse<PostResponseDto> response = new ApiResponse<>(true, "", "", responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
