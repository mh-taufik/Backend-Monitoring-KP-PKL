package com.jtk.ps.api.controller;

import com.jtk.ps.api.dto.supervisor_mapping.SupervisorMappingCreateRequest;
import com.jtk.ps.api.dto.supervisor_mapping.SupervisorMappingResponse;
import com.jtk.ps.api.dto.supervisor_mapping.SupervisorMappingUpdateRequest;
import com.jtk.ps.api.service.IMonitoringService;
import com.jtk.ps.api.util.Constant;
import com.jtk.ps.api.util.ResponseHandler;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/supervisor-mapping")
public class SupervisorMappingController {
    @Autowired
    private IMonitoringService monitoringService;

    @GetMapping("/get")
    public ResponseEntity<Object> getSupervisorMappingByLecturer(@RequestParam(value = "lecturer_id", required = false) Integer lecturerId, @RequestParam(value = "participant_id", required = false) Integer participantId, @RequestParam(value = "prodi_id", required = false) Integer prodiId, @RequestParam(value = "year", required = false) Integer year, HttpServletRequest request) {
        try {
            String cookie = request.getHeader(Constant.PayloadResponseConstant.COOKIE);
            List<SupervisorMappingResponse> response = monitoringService.getSupervisorMapping(cookie);
            return ResponseHandler.generateResponse("Get Supervisor Mapping succeed", HttpStatus.OK, response);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("/get")
//    public ResponseEntity<Object> getSupervisorMappingByLecturer(@RequestParam(value = "lecturer_id", required = false) Integer lecturerId, @RequestParam(value = "participant_id", required = false) Integer participantId, @RequestParam(value = "prodi_id", required = false) Integer prodiId, @RequestParam(value = "year", required = false) Integer year, HttpServletRequest request) {
//        try {
//            if(lecturerId != null){
//                List<SupervisorMappingResponse> response = monitoringService.getSupervisorMappingByLecturer(lecturerId);
//                return ResponseHandler.generateResponse("Get Supervisor Mapping succeed", HttpStatus.OK, response);
//            }
//            if(participantId != null){
//                SupervisorMappingResponse response = monitoringService.getSupervisorMappingByParticipant(lecturerId);
//                return ResponseHandler.generateResponse("Get Supervisor Mapping succeed", HttpStatus.OK, response);
//            }
//            if(prodiId != null){
//                List<SupervisorMappingResponse> response = monitoringService.getSupervisorMappingByProdi(prodiId);
//                return ResponseHandler.generateResponse("Get Supervisor Mapping succeed", HttpStatus.OK, response);
//            }
//            if(year != null){
//                List<SupervisorMappingResponse> response = monitoringService.getSupervisorMappingByProdi(prodiId);
//                return ResponseHandler.generateResponse("Get Supervisor Mapping succeed", HttpStatus.OK, response);
//            }
//            List<SupervisorMappingResponse> response = monitoringService.getSupervisorMapping();
//            return ResponseHandler.generateResponse("Get Supervisor Mapping succeed", HttpStatus.OK, response);
//            return ResponseHandler.generateResponse("Get Supervisor Mapping succeed", HttpStatus.OK);
//        } catch (HttpClientErrorException ex){
//            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @PostMapping("/create")
//    public ResponseEntity<Object> createSupervisorMapping(@RequestBody List<SupervisorMappingCreateRequest> supervisorMappingCreateRequest, HttpServletRequest request){
//        try {
//            monitoringService.createSupervisorMapping(supervisorMappingCreateRequest);
//            return ResponseHandler.generateResponse("Create Supervisor Mapping succeed", HttpStatus.OK);
//        } catch (Exception e) {
//            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @PostMapping("/update")
//    public ResponseEntity<Object> updateSupervisorMapping(@RequestBody List<SupervisorMappingUpdateRequest> supervisorMappingUpdateRequest, HttpServletRequest request){
//        try {
//            monitoringService.updateSupervisorMapping(supervisorMappingUpdateRequest);
//            return ResponseHandler.generateResponse("Update Supervisor Mapping succeed", HttpStatus.OK);
//        } catch (Exception e) {
//            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
