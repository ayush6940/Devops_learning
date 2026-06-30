package com.example.geography.dto;

public class StateRequest {
    private String requestId;
    private String state;

    public StateRequest() {
    }

    public StateRequest(String requestId, String state) {
        this.requestId = requestId;
        this.state = state;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}