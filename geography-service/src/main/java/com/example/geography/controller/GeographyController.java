package com.example.geography.controller;

import com.example.geography.service.GeographyService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/geography")
public class GeographyController {

    private final GeographyService geographyService;

    public GeographyController(GeographyService geographyService) {
        this.geographyService = geographyService;
    }

    @GetMapping("/health")
    public Map<String, String> getHealth() {
        return geographyService.getHealthStatus();
    }

    @GetMapping("/capital")
    public Map<String, String> getCapitalInfo() {
        return geographyService.getCapitalInfo();
    }

    @PostMapping("/echo")
    public Map<String, String> echoCoordinates(@RequestBody Map<String, String> payload) {
        return geographyService.echoCoordinates(payload);
    }

    @PostMapping("/location")
    public com.example.geography.dto.LocationResponse getLocation(@RequestBody com.example.geography.dto.StateRequest request) {
        return geographyService.mapStateToLocation(request);
    }
}
