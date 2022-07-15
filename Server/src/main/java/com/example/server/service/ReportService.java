package com.example.server.service;

import com.example.server.entities.FilmEntity;
import com.example.server.entities.ReportEntity;
import com.example.server.repository.ReportRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
@Component
@Transactional
@Service
public class ReportService {
    @Autowired
    private ReportRepository reportRepository;




    public List <ReportEntity> getAlleReports(){
        return this.reportRepository.findAll();
    }

    public ResponseEntity<ReportEntity> reportAufnehmen (ReportEntity reportEntity){
        this.reportRepository.save(reportEntity);
        return new ResponseEntity<> (reportEntity, HttpStatus.OK);
    }
//
//
//    public void deleteById (Long reportID){
//
//        reportRepository.deleteById(reportID);
//
//
//    }


}
