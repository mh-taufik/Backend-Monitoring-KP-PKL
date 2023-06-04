package com.jtk.ps.api.controller;

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
@RequestMapping("/deadline")
public class MonitoringController {
    @Autowired
    private IMonitoringService monitoringService;

    @GetMapping("/dashboard")
    public ResponseEntity<Object> saveRpp(@RequestParam(value = "participant_id") Integer participantId, HttpServletRequest request) {
        try {
//            rppCreateRequest.setParticipantId((Integer) request.getAttribute(Constant.VerifyConstant.ID));
//            monitoringService.createRpp(rppCreateRequest);
            return ResponseHandler.generateResponse("get data dashboard succeed", HttpStatus.OK);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
