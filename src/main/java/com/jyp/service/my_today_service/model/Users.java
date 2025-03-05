package com.jyp.service.my_today_service.model;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 25)
    private String userId; // 사용자 ID
    @Column(nullable = false)
    private String password; // 사용자 비밀번호
    @Column(nullable = false, length = 254)
    private String email; // 사용자 이메일

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>(); // 게시물

    public Users(String userId, String password, String email) {
        this.userId = userId;
        this.password = password;
        this.email = email;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void addPost(Post post) {
        post.setUsers(this);
        posts.add(post);
    }

}
