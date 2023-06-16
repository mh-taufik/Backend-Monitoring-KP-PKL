package com.jtk.ps.api.controller;

import com.jtk.ps.api.dto.supervisor_mapping.SupervisorMappingLecturerResponse;
import com.jtk.ps.api.dto.supervisor_mapping.SupervisorMappingRequest;
import com.jtk.ps.api.dto.supervisor_mapping.SupervisorMappingResponse;
import com.jtk.ps.api.model.ERole;
import com.jtk.ps.api.service.IMonitoringService;
import com.jtk.ps.api.util.Constant;
import com.jtk.ps.api.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/supervisor-mapping")
public class SupervisorMappingController {
    @Autowired
    private IMonitoringService monitoringService;

    @GetMapping("/is-final")
    public ResponseEntity<Object> isFinalSupervisorMapping(HttpServletRequest request) {
        try {
            String cookie = request.getHeader(Constant.PayloadResponseConstant.COOKIE);
            int prodi = (int) request.getAttribute(Constant.VerifyConstant.ID_PRODI);
            return ResponseHandler.generateResponse("get Final Status Mapping ", HttpStatus.BAD_REQUEST, monitoringService.isFinalSupervisorMapping(cookie, prodi));
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<Object> getSupervisorMappingByProdi(@RequestParam(value = "year", required = false) Integer year, @RequestParam(value = "type", required = false) String type, HttpServletRequest request) {
        try {
            String cookie = request.getHeader(Constant.PayloadResponseConstant.COOKIE);
            int prodi = (int) request.getAttribute(Constant.VerifyConstant.ID_PRODI);
            int id = (int) request.getAttribute(Constant.VerifyConstant.ID);
            Integer role = (Integer) request.getAttribute(Constant.VerifyConstant.ID_ROLE);
            if(year != null){
                List<SupervisorMappingResponse> response = monitoringService.getSupervisorMappingByYear(cookie, year);
                return ResponseHandler.generateResponse("Get Supervisor Mapping in " + year +" succeed", HttpStatus.OK, response);
            }
            if(Objects.equals(type, "full")){
                List<SupervisorMappingResponse> response = monitoringService.getSupervisorMapping(cookie);
                return ResponseHandler.generateResponse("Get All Supervisor Mapping succeed", HttpStatus.OK, response);
            }
            if(role == ERole.COMMITTEE.id){
                List<SupervisorMappingResponse> response = monitoringService.getSupervisorMappingCommittee(cookie, prodi);
                return ResponseHandler.generateResponse("Get Supervisor Mapping succeed", HttpStatus.OK, response);
            }
            if(role == ERole.SUPERVISOR.id){
                List<SupervisorMappingResponse> response = monitoringService.getSupervisorMappingLecturer(cookie, id);
                return ResponseHandler.generateResponse("Get Supervisor Mapping succeed", HttpStatus.OK, response);
            }
            if(role == ERole.PARTICIPANT.id){
                SupervisorMappingResponse response = monitoringService.getSupervisorMappingByParticipant(cookie, id);
                return ResponseHandler.generateResponse("Get Supervisor Mapping succeed", HttpStatus.OK, response);
            }

            List<SupervisorMappingResponse> response = monitoringService.getSupervisorMapping(cookie);
            return ResponseHandler.generateResponse("Get All Supervisor Mapping succeed", HttpStatus.OK, response);

        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('COMMITTEE')")
    public ResponseEntity<Object> createSupervisorMapping(@RequestBody List<SupervisorMappingRequest> supervisorMappingRequest, HttpServletRequest request){
        try {
            String cookie = request.getHeader(Constant.PayloadResponseConstant.COOKIE);
            int creatorId = (Integer) request.getAttribute(Constant.VerifyConstant.ID);
            monitoringService.createSupervisorMapping(supervisorMappingRequest, cookie, creatorId);
            return ResponseHandler.generateResponse("Create Supervisor Mapping succeed", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('COMMITTEE')")
    public ResponseEntity<Object> updateSupervisorMapping(@RequestBody List<SupervisorMappingRequest> supervisorMappingRequest, HttpServletRequest request){
        try {
            String cookie = request.getHeader(Constant.PayloadResponseConstant.COOKIE);
            int creatorId = (Integer) request.getAttribute(Constant.VerifyConstant.ID);
            monitoringService.updateSupervisorMapping(supervisorMappingRequest, cookie, creatorId);
            return ResponseHandler.generateResponse("Update Supervisor Mapping succeed", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
