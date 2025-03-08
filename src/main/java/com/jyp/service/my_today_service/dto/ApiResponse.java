package com.jyp.service.my_today_service.dto;


public class ApiResponse<T> {
    private boolean isSuccess;
    private String message;
    private String errorCode;
    private T data;

    public ApiResponse(boolean isSuccess, String message, String errorCode, T data) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.errorCode = errorCode;
        this.data = data;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
