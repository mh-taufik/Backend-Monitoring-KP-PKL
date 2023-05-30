package com.jtk.ps.api.controller;

import com.jtk.ps.api.dto.laporan.LaporanCreateRequest;
import com.jtk.ps.api.dto.laporan.LaporanResponse;
import com.jtk.ps.api.dto.laporan.LaporanUpdateRequest;
import com.jtk.ps.api.service.IMonitoringService;
import com.jtk.ps.api.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/laporan")
public class LaporanController {
    @Autowired
    private IMonitoringService monitoringService;

    @GetMapping("/list/{id_participant}")
    public ResponseEntity<Object> getListLaporan(@PathVariable("id_participant") Integer participantId, HttpServletRequest request) {
        try {
            List<LaporanResponse> listLaporan = monitoringService.getListLaporan(participantId);
            return ResponseHandler.generateResponse("Get List Laporan succeed", HttpStatus.OK, listLaporan);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/detail/{id_laporan}")
    public ResponseEntity<Object> getLaporan(@PathVariable("id_laporan") Integer idLaporan, HttpServletRequest request) {
        try {
            LaporanResponse laporan = monitoringService.getLaporan(idLaporan);
            return ResponseHandler.generateResponse("Get Laporan succeed", HttpStatus.OK, laporan);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createLaporan(@RequestBody LaporanCreateRequest laporanCreateRequest, HttpServletRequest request){
        try {
            monitoringService.createLaporan(laporanCreateRequest);
            return ResponseHandler.generateResponse("Create Laporan succeed", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateLaporan(@RequestBody LaporanUpdateRequest laporanUpdateRequest, HttpServletRequest request){
        try {
            monitoringService.updateLaporan(laporanUpdateRequest);
            return ResponseHandler.generateResponse("Update Laporan succeed", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
