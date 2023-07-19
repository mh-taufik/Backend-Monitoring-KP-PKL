package com.jtk.ps.api.integration;

import com.jtk.ps.api.MonitoringServiceApplication;
import com.jtk.ps.api.dto.Response;
import com.jtk.ps.api.dto.ResponseList;
import com.jtk.ps.api.dto.supervisor_mapping.SupervisorMappingResponse;
import com.jtk.ps.api.util.Constant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MonitoringServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Execution(value = ExecutionMode.CONCURRENT)
public class SupervisorMappingControllerTest extends IntegrationTest{
    @Test
    void getSupervisorMappingForParticipant(){
        accessToken = getAccessToken(Constant.Role.PARTICIPANT);
        headers.add("COOKIE", accessToken);

        ResponseEntity<Response<SupervisorMappingResponse>> responseEntity = restTemplate.exchange(
                createURLWithPort("/monitoring/supervisor-mapping/get-all"),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void getSupervisorMappingForSupervisor(){
        accessToken = getAccessToken(Constant.Role.COMMITTEE);
        headers.add("COOKIE", accessToken);

        ResponseEntity<Response<SupervisorMappingResponse>> responseEntity = restTemplate.exchange(
                createURLWithPort("/monitoring/supervisor-mapping/get-all"),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void getSupervisorMappingForCommittee(){
        accessToken = getAccessToken(Constant.Role.COMMITTEE);
        headers.add("COOKIE", accessToken);

        ResponseEntity<ResponseList<SupervisorMappingResponse>> responseEntity = restTemplate.exchange(
                createURLWithPort("/monitoring/supervisor-mapping/get-all"),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void getAllSupervisorMapping(){
        accessToken = getAccessToken(Constant.Role.COMMITTEE);
        headers.add("COOKIE", accessToken);

        ResponseEntity<ResponseList<SupervisorMappingResponse>> responseEntity = restTemplate.exchange(
                createURLWithPort("/monitoring/supervisor-mapping/get-all?type=full"),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }
}
