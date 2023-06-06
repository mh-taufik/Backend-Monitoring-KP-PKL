package com.jtk.ps.api.controller;

import com.google.common.base.Objects;
import com.jtk.ps.api.dto.DashboardCommittee;
import com.jtk.ps.api.dto.DashboardLecturer;
import com.jtk.ps.api.dto.DashboardParticipant;
import com.jtk.ps.api.dto.rpp.RppCreateRequest;
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

@RestController
@RequestMapping("")
public class MonitoringController {
    @Autowired
    private IMonitoringService monitoringService;

    @GetMapping("/dashboard")
    public ResponseEntity<Object> getDashboardParticipant(HttpServletRequest request) {
        try {
            if(request.getAttribute(Constant.VerifyConstant.ID_ROLE) == Constant.Role.PARTICIPANT) {
                DashboardParticipant response = monitoringService.getDashboardDataParticipant((Integer) request.getAttribute(Constant.VerifyConstant.ID));
                return ResponseHandler.generateResponse("get data dashboard succeed", HttpStatus.OK, response);
            }else if(request.getAttribute(Constant.VerifyConstant.ID_ROLE) == Constant.Role.SUPERVISOR){
                DashboardLecturer response = monitoringService.getDashboardDataLecturer((Integer) request.getAttribute(Constant.VerifyConstant.ID));
                return ResponseHandler.generateResponse("get data dashboard succeed", HttpStatus.OK, response);
            }else if(request.getAttribute(Constant.VerifyConstant.ID_ROLE) == Constant.Role.SUPERVISOR){
                DashboardCommittee response = monitoringService.getDashboardDataCommittee((Integer) request.getAttribute(Constant.VerifyConstant.ID_PRODI));
                return ResponseHandler.generateResponse("get data dashboard succeed", HttpStatus.OK, response);
            }
            return ResponseHandler.generateResponse("get data dashboard succeed", HttpStatus.OK);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
