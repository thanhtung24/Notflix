package com.example.server.restcontroller;


import com.example.server.entities.PersonEntity;
import com.example.server.entities.ReportEntity;
import com.example.server.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportRestController {

    @Autowired
    private ReportService reportService;


    @GetMapping("/alleReports")
    public List<ReportEntity> getAlleReports() {
        return this.reportService.getAlleReports();
    }


    @PostMapping("/ReportAufnehmen")
    public ResponseEntity<ReportEntity> reportAufnehmen (@RequestBody ReportEntity reportEntity){
        return this.reportService.reportAufnehmen(reportEntity);
    }

}
