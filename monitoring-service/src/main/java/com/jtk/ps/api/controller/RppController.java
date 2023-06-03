package com.jtk.ps.api.controller;

import com.jtk.ps.api.dto.logbook.LogbookCreateRequest;
import com.jtk.ps.api.dto.logbook.LogbookUpdateRequest;
import com.jtk.ps.api.dto.rpp.RppCreateRequest;
import com.jtk.ps.api.dto.rpp.RppDetailResponse;
import com.jtk.ps.api.dto.rpp.RppResponse;
import com.jtk.ps.api.dto.rpp.RppUpdateRequest;
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

@RestController
@RequestMapping("/rpp")
public class RppController {
    @Autowired
    private IMonitoringService monitoringService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('PARTICIPANT')")
    public ResponseEntity<Object> saveRpp(@RequestBody RppCreateRequest rppCreateRequest, HttpServletRequest request) {
        try {
            rppCreateRequest.setParticipantId((Integer) request.getAttribute(Constant.VerifyConstant.ID));
            monitoringService.createRpp(rppCreateRequest);
            return ResponseHandler.generateResponse("Save Rpp succeed", HttpStatus.OK);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('PARTICIPANT')")
    public ResponseEntity<Object> updateRpp(@RequestBody RppUpdateRequest rppUpdateRequest, HttpServletRequest request) {
        try {
            monitoringService.updateRpp(rppUpdateRequest);
            return ResponseHandler.generateResponse("Update Rpp succeed", HttpStatus.OK);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-all/{participant_id}")
    @PreAuthorize("hasAnyAuthority('COMMITTEE','PARTICIPANT','SUPERVISOR')")
    public ResponseEntity<Object> getRppList(@PathVariable("participant_id") Integer participantId, HttpServletRequest request) {
        try {
            List<RppResponse> rppList = monitoringService.getRppList(participantId);
            return ResponseHandler.generateResponse("Get RPP list succeed", HttpStatus.OK, rppList);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{id_rpp}")
    @PreAuthorize("hasAnyAuthority('COMMITTEE','PARTICIPANT','SUPERVISOR')")
    public ResponseEntity<Object> getRppDetail(@PathVariable("id_rpp") Integer idRpp, HttpServletRequest request) {
        try {
            RppDetailResponse rpp = monitoringService.getRppDetail(idRpp);
            return ResponseHandler.generateResponse("Get RPP succeed", HttpStatus.OK, rpp);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
