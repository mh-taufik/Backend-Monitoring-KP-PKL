package com.jtk.ps.api.controller;

import com.jtk.ps.api.dto.laporan.LaporanResponse;
import com.jtk.ps.api.dto.self_assessment.SelfAssessmentDetailResponse;
import com.jtk.ps.api.dto.self_assessment.SelfAssessmentResponse;
import com.jtk.ps.api.service.IMonitoringService;
import com.jtk.ps.api.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/self-assessment")
public class SelfAssessmentController {
    @Autowired
    private IMonitoringService monitoringService;

    @GetMapping("/detail/{id_self_assessment}")
    public ResponseEntity<Object> getSelfAssessmentDetail(@PathVariable("id_self_assessment") Integer idSelfAssessment, HttpServletRequest request) {
        try {
            SelfAssessmentDetailResponse response = monitoringService.getSelfAssessmentDetail(idSelfAssessment);
            return ResponseHandler.generateResponse("Get RPP succeed", HttpStatus.OK, response);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list/{id_participant}")
    public ResponseEntity<Object> getSelfAssessmentList(@PathVariable("id_participant") Integer idParticipant, HttpServletRequest request) {
        try {
             List<SelfAssessmentResponse> response = monitoringService.getSelfAssessmentList(idParticipant);
            return ResponseHandler.generateResponse("Get RPP succeed", HttpStatus.OK, response);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
