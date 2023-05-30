package com.jtk.ps.api.controller;

import com.jtk.ps.api.dto.rpp.RppDetailResponse;
import com.jtk.ps.api.dto.rpp.RppResponse;
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
@RequestMapping("/rpp")
public class RppController {
    @Autowired
    private IMonitoringService monitoringService;

    @GetMapping("/list/{participant_id}")
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

    @GetMapping("/detail/{id_rpp}")
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
