package com.example.geography.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;

@Service
public class GeographyService {

    public Map<String, String> getHealthStatus() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "geography-service");
        return status;
    }

    public Map<String, String> getCapitalInfo() {
        Map<String, String> response = new HashMap<>();
        response.put("country", "France");
        response.put("capital", "Paris");
        return response;
    }

    public Map<String, String> echoCoordinates(Map<String, String> payload) {
        Map<String, String> response = new HashMap<>();
        response.put("received_coordinates", payload.toString());
        response.put("status", "Coordinates received successfully");
        return response;
    }

    public com.example.geography.dto.LocationResponse mapStateToLocation(com.example.geography.dto.StateRequest request) {
        com.example.geography.dto.LocationResponse response = new com.example.geography.dto.LocationResponse();
        response.setRequestId(request.getRequestId());
        response.setState(request.getState());

        if (request.getState() == null) {
            response.setCity("Unknown");
            response.setArea("Unknown");
            response.setRoad("Unknown");
            return response;
        }

        switch (request.getState().trim()) {
            case "Karnataka":
                response.setCity("Bangalore");
                response.setArea("Whitefield");
                response.setRoad("ITPL Main Road");
                break;
            case "Tamil Nadu":
                response.setCity("Chennai");
                response.setArea("Guindy");
                response.setRoad("GST Road");
                break;
            case "Maharashtra":
                response.setCity("Mumbai");
                response.setArea("Andheri");
                response.setRoad("Link Road");
                break;
            case "Delhi":
                response.setCity("New Delhi");
                response.setArea("Connaught Place");
                response.setRoad("Janpath");
                break;
            default:
                response.setCity("Unknown");
                response.setArea("Unknown");
                response.setRoad("Unknown");
        }
        return response;
    }
}
