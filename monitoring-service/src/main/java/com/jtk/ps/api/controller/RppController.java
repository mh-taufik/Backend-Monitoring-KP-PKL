package com.jtk.ps.api.controller;

import com.jtk.ps.api.dto.CreateId;
import com.jtk.ps.api.dto.rpp.*;
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
            Integer participantId = (Integer) request.getAttribute(Constant.VerifyConstant.ID);
            CreateId id = monitoringService.createRpp(rppCreateRequest, participantId);
            return ResponseHandler.generateResponse("Save Rpp succeed", HttpStatus.OK, id);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create/simple")
    @PreAuthorize("hasAnyAuthority('PARTICIPANT')")
    public ResponseEntity<Object> saveRpp(@RequestBody RppSimpleCreateRequest rppCreateRequest, HttpServletRequest request) {
        try {
            Integer participantId = (Integer) request.getAttribute(Constant.VerifyConstant.ID);
            CreateId id = monitoringService.createRpp(rppCreateRequest, participantId);
            return ResponseHandler.generateResponse("Save Rpp succeed", HttpStatus.OK, id);
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
            Integer participantId = (Integer) request.getAttribute(Constant.VerifyConstant.ID);
            monitoringService.updateRpp(rppUpdateRequest, participantId);
            return ResponseHandler.generateResponse("Update Rpp succeed", HttpStatus.OK);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/simple")
    @PreAuthorize("hasAnyAuthority('PARTICIPANT')")
    public ResponseEntity<Object> updateRpp(@RequestBody RppSimpleUpdateRequest rppUpdateRequest, HttpServletRequest request) {
        try {
            Integer participantId = (Integer) request.getAttribute(Constant.VerifyConstant.ID);
            monitoringService.updateRpp(rppUpdateRequest, participantId);
            return ResponseHandler.generateResponse("Update Rpp succeed", HttpStatus.OK);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/milestone/create")
    @PreAuthorize("hasAnyAuthority('PARTICIPANT')")
    public ResponseEntity<Object> saveMilestone(@RequestBody CustomRequest customRequest, HttpServletRequest request) {
        try {
            monitoringService.createMilestone(customRequest.getMilestone(), customRequest.getRppId());
            return ResponseHandler.generateResponse("Save Milestone succeed", HttpStatus.OK);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/milestone/update")
    @PreAuthorize("hasAnyAuthority('PARTICIPANT')")
    public ResponseEntity<Object> updateMilestone(@RequestBody CustomRequest customRequest, HttpServletRequest request) {
        try {
            monitoringService.updateMilestone(customRequest.getMilestone(), customRequest.getRppId());
            return ResponseHandler.generateResponse("Update Milestone succeed", HttpStatus.OK);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/deliverable/create")
    @PreAuthorize("hasAnyAuthority('PARTICIPANT')")
    public ResponseEntity<Object> saveDeliverables(@RequestBody CustomRequest customRequest, HttpServletRequest request) {
        try {
            monitoringService.createDeliverables(customRequest.getDeliverables(), customRequest.getRppId());
            return ResponseHandler.generateResponse("Save Deliverables succeed", HttpStatus.OK);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/deliverable/update")
    @PreAuthorize("hasAnyAuthority('PARTICIPANT')")
    public ResponseEntity<Object> updateDeliverables(@RequestBody CustomRequest customRequest, HttpServletRequest request) {
        try {
            monitoringService.updateDeliverables(customRequest.getDeliverables(), customRequest.getRppId());
            return ResponseHandler.generateResponse("Update Deliverables succeed", HttpStatus.OK);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/completion-schedule/create")
    @PreAuthorize("hasAnyAuthority('PARTICIPANT')")
    public ResponseEntity<Object> saveCompletionSchedule(@RequestBody CustomRequest customRequest, HttpServletRequest request) {
        try {
            monitoringService.createCompletionSchedule(customRequest.getCompletionSchedule(), customRequest.getRppId());
            return ResponseHandler.generateResponse("Save Completion Schedule succeed", HttpStatus.OK);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/completion-schedule/update")
    @PreAuthorize("hasAnyAuthority('PARTICIPANT')")
    public ResponseEntity<Object> updateCompletionSchedule(@RequestBody CustomRequest customRequest, HttpServletRequest request) {
        try {
            monitoringService.updateCompletionSchedule(customRequest.getCompletionSchedule(), customRequest.getRppId());
            return ResponseHandler.generateResponse("Update Completion Schedule succeed", HttpStatus.OK);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/weekly-achievement/create")
    @PreAuthorize("hasAnyAuthority('PARTICIPANT')")
    public ResponseEntity<Object> saveWeeklyAchievementPlan(@RequestBody CustomRequest customRequest, HttpServletRequest request) {
        try {
            monitoringService.createWeeklyAchievementPlan(customRequest.getWeeklyAchievementPlan(), customRequest.getRppId());
            return ResponseHandler.generateResponse("Save Weekly Achievement Plan succeed", HttpStatus.OK);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/weekly-achievement/update")
    @PreAuthorize("hasAnyAuthority('PARTICIPANT')")
    public ResponseEntity<Object> updateWeeklyAchievementPlan(@RequestBody CustomRequest customRequest, HttpServletRequest request) {
        try {
            monitoringService.updateWeeklyAchievementPlan(customRequest.getWeeklyAchievementPlan(), customRequest.getRppId());
            return ResponseHandler.generateResponse("Update Weekly Achievement Plan succeed", HttpStatus.OK);
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
