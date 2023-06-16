package com.jtk.ps.api.controller;

import com.jtk.ps.api.dto.deadline.DeadlineCreateRequest;
import com.jtk.ps.api.dto.deadline.DeadlineResponse;
import com.jtk.ps.api.dto.deadline.DeadlineUpdateRequest;
import com.jtk.ps.api.service.IMonitoringService;
import com.jtk.ps.api.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/deadline")
public class DeadlineController {
    @Autowired
    private IMonitoringService monitoringService;

    @GetMapping("/get-all")
    public ResponseEntity<Object> getDeadline(@RequestParam(value = "id_deadline", required = false) Integer deadlineId, HttpServletRequest request) {
        try {
            if(deadlineId != null){
                DeadlineResponse response = monitoringService.getDeadline(deadlineId);
                return ResponseHandler.generateResponse("Get Deadline succeed", HttpStatus.OK, response);
            }
            List<DeadlineResponse> response = monitoringService.getDeadline();
            return ResponseHandler.generateResponse("Get List Deadline succeed", HttpStatus.OK, response);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-all/laporan")
    public ResponseEntity<Object> getDeadlineLaporan(HttpServletRequest request) {
        try {
            List<DeadlineResponse> response = monitoringService.getDeadlineLaporan();
            return ResponseHandler.generateResponse("Get List Deadline succeed", HttpStatus.OK, response);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('COMMITTEE')")
    public ResponseEntity<Object> createDeadline(@RequestBody DeadlineCreateRequest deadlineCreateRequest, HttpServletRequest request){
        try {
            monitoringService.createDeadline(deadlineCreateRequest);
            return ResponseHandler.generateResponse("Create Deadline succeed", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('COMMITTEE')")
    public ResponseEntity<Object> updateDeadline(@RequestBody DeadlineUpdateRequest deadlineUpdateRequest, HttpServletRequest request){
        try {
            monitoringService.updateDeadline(deadlineUpdateRequest);
            return ResponseHandler.generateResponse("Update Deadline succeed", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
