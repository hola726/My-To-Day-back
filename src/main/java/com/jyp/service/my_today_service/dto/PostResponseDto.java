package com.jyp.service.my_today_service.dto;

///  게시물 respone dto
public class PostResponseDto {
    private Long id;
    private String imagePath;
    private String content;
    private Double latitude;
    private Double longitude;
    private Long userId;

    public PostResponseDto() {
    }

    public PostResponseDto(Long id, String imagePath, String content, Double latitude, Double longitude, Long userId) {
        this.id = id;
        this.imagePath = imagePath;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
