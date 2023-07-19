package com.jtk.ps.api.integration;

import com.jtk.ps.api.MonitoringServiceApplication;
import com.jtk.ps.api.dto.Response;
import com.jtk.ps.api.dto.ResponseList;
import com.jtk.ps.api.dto.deadline.DeadlineResponse;
import com.jtk.ps.api.dto.logbook.LogbookDetailResponse;
import com.jtk.ps.api.dto.logbook.LogbookResponse;
import com.jtk.ps.api.util.Constant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MonitoringServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Execution(value = ExecutionMode.CONCURRENT)
public class LogbookControllerTest extends IntegrationTest{
    @Test
    void getLogbookByParticipantId(){
        accessToken = getAccessToken(Constant.Role.PARTICIPANT);
        headers.add("COOKIE", accessToken);

        ResponseEntity<ResponseList<LogbookResponse>> responseEntity = restTemplate.exchange(
                createURLWithPort("/monitoring/logbook/get-all/201524008"),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void getLogbookById(){
        accessToken = getAccessToken(Constant.Role.PARTICIPANT);
        headers.add("COOKIE", accessToken);

        ResponseEntity<Response<LogbookDetailResponse>> responseEntity = restTemplate.exchange(
                createURLWithPort("/monitoring/logbook/get/"),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void checkLogbook(){
        accessToken = getAccessToken(Constant.Role.PARTICIPANT);
        headers.add("COOKIE", accessToken);
        String body = "{\"date\": \"2023-06-29\"}";
        headers.setContentType(MediaType.APPLICATION_JSON,);

        ResponseEntity<ResponseList<DeadlineResponse>> responseEntity = restTemplate.exchange(
                createURLWithPort("/monitoring/deadline/get-all/laporan"),
                HttpMethod.GET,
                new HttpEntity<>(body, headers),
                new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

    }
}
