package com.example.server.service;


import com.example.server.entities.ReportEntity;
import com.example.server.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Component
@Service
@Transactional
public class ReportanfrageAdminService {

    @Autowired
    private ReportRepository reportRepository;

  //  public ResponseEntity<ReportEntity> bearbeiteReport(Long reportId) {
   // }

/*    public boolean deleteReport (ReportEntity reportEntity){
        boolean check =false;
        try {
            this.reportRepository.delete(reportEntity);
            if (reportEntity.getId().equals(Optional.empty())) {
                check =true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return check;
    }*/

//    private ResponseEntity<ReportEntity> bearbeiteReport(Long reportId){
//        ReportEntity report =this.reportRepository.getById(reportId);
//
//
//    }




}
