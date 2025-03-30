package com.jyp.service.my_today_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class PostRequestDto {

    @Schema(description = "게시물 이미지")
    private String imagePath;
    @Schema(description = "게시물 본문")
    @Size(min = 1, max = 800, message = "게시물 글자 수는 최소 1자 최대 800자입니다.")
    @NotEmpty(message = "게시물 본문은 필수 입니다.")
    private String content;
    @Schema(description = "위도")
    private Double latitude;
    @Schema(description = "경도")
    private Double longitude;

    public PostRequestDto() {
    }

    public PostRequestDto(String imagePath, String content, Double latitude, Double longitude) {
        this.imagePath = imagePath;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public boolean isEmpty() {
        return imagePath == null || content == null || latitude == null || longitude == null;
    }
}
