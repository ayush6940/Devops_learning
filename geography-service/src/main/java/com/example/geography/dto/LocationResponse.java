package com.example.geography.dto;

public class LocationResponse {
    private String requestId;
    private String state;
    private String city;
    private String area;
    private String road;

    public LocationResponse() {
    }

    public LocationResponse(String requestId, String state, String city, String area, String road) {
        this.requestId = requestId;
        this.state = state;
        this.city = city;
        this.area = area;
        this.road = road;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    @Override
    public String toString() {
        return "LocationResponse{" +
                "requestId='" + requestId + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", road='" + road + '\'' +
                '}';
    }
}
