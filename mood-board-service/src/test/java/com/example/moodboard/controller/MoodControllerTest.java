package com.example.moodboard.controller;

import com.example.moodboard.dto.StateRequest;
import com.example.moodboard.service.MoodService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MoodController.class)
class MoodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MoodService moodService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetHealth() throws Exception {
        Map<String, String> healthStatus = new HashMap<>();
        healthStatus.put("status", "UP");
        
        when(moodService.getHealthStatus()).thenReturn(healthStatus);

        mockMvc.perform(get("/api/mood/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void testGetHello() throws Exception {
        Map<String, String> helloResponse = new HashMap<>();
        helloResponse.put("message", "Hello from the Mood Board Service!");
        
        when(moodService.generateMoodMessage()).thenReturn(helloResponse);

        mockMvc.perform(get("/api/mood/hello"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello from the Mood Board Service!"));
    }

    @Test
    void testEcho() throws Exception {
        Map<String, String> requestPayload = new HashMap<>();
        requestPayload.put("mood", "happy");
        
        Map<String, String> responsePayload = new HashMap<>();
        responsePayload.put("status", "Echoed successfully");
        
        when(moodService.echoMessage(any())).thenReturn(responsePayload);

        mockMvc.perform(post("/api/mood/echo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestPayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Echoed successfully"));
    }

    @Test
    void testReceiveState() throws Exception {
        StateRequest request = new StateRequest();
        request.setRequestId("req-2");
        request.setState("Karnataka");
        
        when(moodService.processStateRequest(any(StateRequest.class))).thenReturn(request);

        mockMvc.perform(post("/api/mood/state")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value("req-2"))
                .andExpect(jsonPath("$.state").value("Karnataka"));
    }
}
