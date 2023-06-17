package com.jtk.ps.api.controller;

import com.jtk.ps.api.dto.CheckLaporan;
import com.jtk.ps.api.dto.CreateId;
import com.jtk.ps.api.dto.laporan.LaporanCreateRequest;
import com.jtk.ps.api.dto.laporan.LaporanResponse;
import com.jtk.ps.api.dto.laporan.LaporanUpdateRequest;
import com.jtk.ps.api.service.IMonitoringService;
import com.jtk.ps.api.util.Constant;
import com.jtk.ps.api.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/laporan")
public class LaporanController {
    @Autowired
    private IMonitoringService monitoringService;

    @GetMapping("/get-all/{id_participant}")
    @PreAuthorize("hasAnyAuthority('COMMITTEE','PARTICIPANT','SUPERVISOR')")
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

    @GetMapping("/get/{id_laporan}")
    @PreAuthorize("hasAnyAuthority('COMMITTEE','PARTICIPANT','SUPERVISOR')")
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

//    @PostMapping("/upload")
//    @PreAuthorize("hasAnyAuthority('PARTICIPANT')")
//    public ResponseEntity<Object> uploadLaporan(@RequestParam("phase") Integer phase, @RequestParam("file") MultipartFile file, HttpServletRequest request){
//        try {
//            Integer participantId = (Integer) request.getAttribute(Constant.VerifyConstant.ID);
//            CreateId id = monitoringService.uploadLaporan(, laporanUploadRequest, , participantId);
//            return ResponseHandler.generateResponse("Create Laporan succeed", HttpStatus.OK, id);
//        } catch (Exception e) {
//            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('PARTICIPANT')")
    public ResponseEntity<Object> createLaporan(@RequestBody LaporanCreateRequest laporanCreateRequest, HttpServletRequest request){
        try {
            Integer participantId = (Integer) request.getAttribute(Constant.VerifyConstant.ID);
            CreateId id = monitoringService.createLaporan(laporanCreateRequest, participantId);
            return ResponseHandler.generateResponse("Create Laporan succeed", HttpStatus.OK, id);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('PARTICIPANT')")
    public ResponseEntity<Object> updateLaporan(@RequestBody LaporanUpdateRequest laporanUpdateRequest, HttpServletRequest request){
        try {
            Integer participantId = (Integer) request.getAttribute(Constant.VerifyConstant.ID);
            monitoringService.updateLaporan(laporanUpdateRequest, participantId);
            return ResponseHandler.generateResponse("Update Laporan succeed", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-phase")
    @PreAuthorize("hasAnyAuthority('COMMITTEE','PARTICIPANT','SUPERVISOR')")
    public ResponseEntity<Object> getLaporanPhase(HttpServletRequest request) {
        try {
            return ResponseHandler.generateResponse("Get Phase Laporan succeed", HttpStatus.OK, monitoringService.getPhase());
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/is-final-phase")
    @PreAuthorize("hasAnyAuthority('COMMITTEE','PARTICIPANT','SUPERVISOR')")
    public ResponseEntity<Object> isFinalPhase(HttpServletRequest request) {
        try {
            return ResponseHandler.generateResponse("Get Phase Laporan succeed", HttpStatus.OK, monitoringService.isFinalPhase());
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/check")
    @PreAuthorize("hasAnyAuthority('PARTICIPANT')")
    public ResponseEntity<Object> checkByDate(@RequestBody CheckLaporan check, HttpServletRequest request){
        try {
            Integer id = (Integer) Objects.requireNonNull(request.getAttribute(Constant.VerifyConstant.ID));
            return ResponseHandler.generateResponse("Check by phase succeed", HttpStatus.OK, monitoringService.isLaporanExist(id, check.getPhase()));
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
