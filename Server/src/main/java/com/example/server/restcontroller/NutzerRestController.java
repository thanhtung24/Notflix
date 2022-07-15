package com.example.server.restcontroller;

import com.example.server.entities.NutzerEntity;
import com.example.server.service.NutzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nutzer")

public class NutzerRestController {

    @Autowired
    private NutzerService nutzerService;

    @GetMapping("/alleNutzer")
    public List<NutzerEntity> alleNutzer(){
        return nutzerService.getNutzer();
    }

    @PostMapping("/nutzerRegistrierung")
    public ResponseEntity<NutzerEntity> nutzerRegistrierung(@RequestBody NutzerEntity nutzer){
        int code= nutzerService.registriereNutzer(nutzer).getStatusCode().value();

        if(code==200){
            return new ResponseEntity<>(nutzer, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/nutzerLogin")
    public ResponseEntity<NutzerEntity> nutzerLogin(@RequestBody NutzerEntity nutzer){
        return nutzerService.loginNutzer(nutzer);
    }

    @GetMapping("/getNutzer/{id}")
    public ResponseEntity<NutzerEntity> nutzer(@PathVariable(value = "id") Long id){
        return new ResponseEntity<>(nutzerService.findeNutzerAnhandId(id), HttpStatus.OK);
    }
}
