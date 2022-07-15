package com.example.server.restcontroller;

import com.example.server.entities.FilmEntity;
import com.example.server.entities.SystemadministratorEntity;
import com.example.server.service.SystemadministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin")
public class SystemadministratorRestController {

    @Autowired
    private SystemadministratorService systemadministratorService;

    @GetMapping("/alleAdmin")
    public List<SystemadministratorEntity> alleAdmin(){
        return systemadministratorService.getAdmins();
    }


    @GetMapping("/alleAdminEmails")
    public List <SystemadministratorEntity> getalleAdminEmails() {
        return systemadministratorService.getAlleEMailsVonAdmins();
    }


    @PostMapping("/adminRegistrierung")
    public ResponseEntity<SystemadministratorEntity> adminRegistrierung(@RequestBody SystemadministratorEntity systemadministrator){
        int code = systemadministratorService.createAdmin(systemadministrator).getStatusCode().value();
        if(code == 200){
            return new ResponseEntity<>(systemadministrator, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/adminLogin")
    public ResponseEntity<SystemadministratorEntity> adminLogin(@RequestBody SystemadministratorEntity systemadministrator){
        int code = systemadministratorService.adminLogin(systemadministrator).getStatusCode().value();

        if(code == 200){
            return new ResponseEntity<>(systemadministrator, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


}
