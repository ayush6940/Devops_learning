package com.example.geography.controller;

import com.example.geography.dto.LocationResponse;
import com.example.geography.dto.StateRequest;
import com.example.geography.service.GeographyService;
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

@WebMvcTest(GeographyController.class)
class GeographyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeographyService geographyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetHealth() throws Exception {
        Map<String, String> healthStatus = new HashMap<>();
        healthStatus.put("status", "UP");
        
        when(geographyService.getHealthStatus()).thenReturn(healthStatus);

        mockMvc.perform(get("/api/geography/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void testGetCapitalInfo() throws Exception {
        Map<String, String> capitalInfo = new HashMap<>();
        capitalInfo.put("country", "France");
        capitalInfo.put("capital", "Paris");
        
        when(geographyService.getCapitalInfo()).thenReturn(capitalInfo);

        mockMvc.perform(get("/api/geography/capital"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country").value("France"))
                .andExpect(jsonPath("$.capital").value("Paris"));
    }

    @Test
    void testEchoCoordinates() throws Exception {
        Map<String, String> requestPayload = new HashMap<>();
        requestPayload.put("lat", "10.0");
        requestPayload.put("lng", "20.0");
        
        Map<String, String> responsePayload = new HashMap<>();
        responsePayload.put("status", "Coordinates received successfully");
        
        when(geographyService.echoCoordinates(any())).thenReturn(responsePayload);

        mockMvc.perform(post("/api/geography/echo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestPayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Coordinates received successfully"));
    }

    @Test
    void testGetLocation() throws Exception {
        StateRequest request = new StateRequest();
        request.setRequestId("req-1");
        request.setState("Delhi");
        
        LocationResponse response = new LocationResponse();
        response.setRequestId("req-1");
        response.setState("Delhi");
        response.setCity("New Delhi");
        response.setArea("Connaught Place");
        
        when(geographyService.mapStateToLocation(any(StateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/geography/location")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("New Delhi"))
                .andExpect(jsonPath("$.area").value("Connaught Place"));
    }
}
