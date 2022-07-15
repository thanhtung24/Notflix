package com.example.client;

import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class ClientApplication implements CommandLineRunner {

    @Autowired
    private FXMLLoader FXMLLoader;

    private void start() {
        FXMLLoader.initialize();
    }
    public static void main(String[] args) {
        FreePortChooser.setRandomPort(9000,9100);

        SpringApplication.run(ClientApplication.class, args);

    }

    @Override
    public void run(String... args){
        Platform.startup(this::start);

    }
}
