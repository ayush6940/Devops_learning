package com.example.moodboard.controller;

import com.example.moodboard.service.MoodService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/mood")
public class MoodController {

    private final MoodService moodService;

    public MoodController(MoodService moodService) {
        this.moodService = moodService;
    }

    @GetMapping("/health")
    public Map<String, String> getHealth() {
        return moodService.getHealthStatus();
    }

    @GetMapping("/hello")
    public Map<String, String> getHello() {
        return moodService.generateMoodMessage();
    }

    @PostMapping("/echo")
    public Map<String, String> echo(@RequestBody Map<String, String> payload) {
        return moodService.echoMessage(payload);
    }

    @PostMapping("/state")
    public com.example.moodboard.dto.StateRequest receiveState(@RequestBody com.example.moodboard.dto.StateRequest request) {
        return moodService.processStateRequest(request);
    }
}
