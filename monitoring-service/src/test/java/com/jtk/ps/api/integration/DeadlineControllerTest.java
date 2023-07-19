package com.jtk.ps.api.integration;

import com.jtk.ps.api.MonitoringServiceApplication;
import com.jtk.ps.api.controller.DeadlineController;
import com.jtk.ps.api.dto.Response;
import com.jtk.ps.api.dto.ResponseList;
import com.jtk.ps.api.dto.deadline.DeadlineResponse;
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
public class DeadlineControllerTest extends IntegrationTest{
    @Test
    void getDeadlineById(){
        accessToken = getAccessToken(Constant.Role.SUPERVISOR);
        headers.add("COOKIE", accessToken);

        ResponseEntity<Response<DeadlineResponse>> responseEntity = restTemplate.exchange(
                createURLWithPort("/monitoring/deadline/get-all?id=1"),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void getAllDeadline(){
        accessToken = getAccessToken(Constant.Role.SUPERVISOR);
        headers.add("COOKIE", accessToken);

        ResponseEntity<ResponseList<DeadlineResponse>> responseEntity = restTemplate.exchange(
                createURLWithPort("/monitoring/deadline/get-all"),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void getAllDeadlineLaporan(){
        accessToken = getAccessToken(Constant.Role.SUPERVISOR);
        headers.add("COOKIE", accessToken);

        ResponseEntity<ResponseList<DeadlineResponse>> responseEntity = restTemplate.exchange(
                createURLWithPort("/monitoring/deadline/get-all/laporan"),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }
}
