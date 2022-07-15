package com.example.server.service;

import com.example.server.entities.FilmEntity;
import com.example.server.entities.ReportEntity;
import com.example.server.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
@Component
@Service
@Transactional
public class Admin_KommentarService {

    @Autowired
    ReportRepository reportRepository;

    public ResponseEntity <ReportEntity> bearbeiteReport(Long reportId) {

        ReportEntity report = this.reportRepository.getById(reportId);

        try {
            report.setErledigt(true);
            this.reportRepository.save(report);
            return new ResponseEntity<>(report, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println("Fehler beim Bearbeiten vom Report");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
