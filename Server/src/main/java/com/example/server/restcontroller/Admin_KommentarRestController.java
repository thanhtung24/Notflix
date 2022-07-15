package com.example.server.restcontroller;


import com.example.server.entities.FilmEntity;
import com.example.server.entities.ReportEntity;
import com.example.server.service.Admin_KommentarService;
import com.example.server.service.ReportanfrageAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reportErledigt")
public class Admin_KommentarRestController {

    @Autowired
    Admin_KommentarService admin_KommentarService;

    @PutMapping("/erledigt")
    public ResponseEntity <ReportEntity> bearbeiteReport(@RequestParam("reportId") Long reportId) {
                                                             return this.admin_KommentarService.bearbeiteReport(reportId);
    }
}
