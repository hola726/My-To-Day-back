package com.jyp.service.my_today_service.controller;

import com.jyp.service.my_today_service.dto.ApiResponse;
import com.jyp.service.my_today_service.dto.PostDto;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<ApiResponse<PageResponseDto<PostResponseDto>>> getPost(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponseDto<PostResponseDto> pageResponseDto = postService.getPosts(pageable);
        final ApiResponse<PageResponseDto<PostResponseDto>> response = new ApiResponse<>(true, "", "", pageResponseDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시물 등록")
    @Transactional
    @PostMapping("v1.0/post")
    public ResponseEntity<ApiResponse<PostDto>> setPost(@RequestBody @Valid PostDto postDto) {
        // 현재 인증된 사용자 정보 가져오기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userIdFromToken;

        if (principal instanceof UserDetails) {
            userIdFromToken = ((UserDetails) principal).getUsername(); // JWT에서 username 또는 userId로 설정된 값
        } else {
            throw new RuntimeException("인증된 사용자를 찾을 수 없습니다.");
        }

        // UserService를 통해 Users 엔티티 조회
        Users user = userService.findUserByUserId(userIdFromToken)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userIdFromToken));

        final Post post = new Post(
                postDto.getImagePath(),
                postDto.getContent(),
                postDto.getLatitude(),
                postDto.getLongitude()
        );
        post.setUsers(user);
        final Post savePost = postService.savePost(post);

        PostDto responseDto = new PostDto(
                savePost.getImagePath(),
                savePost.getContent(),
                savePost.getLatitude(),
                savePost.getLongitude()
        );
        ApiResponse<PostDto> response = new ApiResponse<>(true, "게시물이 성공적으로 등록되었습니다.", "", responseDto);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @Operation(summary = "게시물 수정")
    @Transactional
    @PutMapping("v1.0/post")
    public ResponseEntity<ApiResponse<PostDto>> updatePost(@RequestBody @Valid PostDto postDto, @RequestParam Long postId) {
        if (postDto.isEmpty()) {
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

        if (postDto.getImagePath().equals(post.get().getImagePath()) && postDto.getContent().equals(post.get().getContent()) && postDto.getLatitude().equals(post.get().getLatitude()) && postDto.getLongitude().equals(post.get().getLongitude())) {
            throw new RuntimeException("수정할 내용이 없습니다.");
        }

        post.get().setImagePath(postDto.getImagePath());
        post.get().setContent(postDto.getContent());
        post.get().setLatitude(postDto.getLatitude());
        post.get().setLongitude(postDto.getLongitude());
        post.get().setUpdatedAt(Instant.now());

        final Post savePost = postService.savePost(post.get());
        final PostDto responseDto = new PostDto(
                savePost.getImagePath(),
                savePost.getContent(),
                savePost.getLatitude(),
                savePost.getLongitude()
        );

        ApiResponse<PostDto> response = new ApiResponse<>(true, "게시물이 성공적으로 수정되었습니다.", "", responseDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "게시물 삭제")
    @DeleteMapping("v1.0/post")
    public ResponseEntity<ApiResponse<PostDto>> deletePost(@RequestParam Long postId) {
        final Optional<Post> post = postService.findPostById(postId);

        if (post.isEmpty()) {
            throw new RuntimeException("게시물을 찾을 수 없습니다.");
        }
        postService.deletePostById(postId);
        final PostDto responseDto = new PostDto(post.get().getImagePath(), post.get().getContent(), post.get().getLatitude(), post.get().getLongitude());
        final ApiResponse<PostDto> response = new ApiResponse<>(true, "", "", responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
