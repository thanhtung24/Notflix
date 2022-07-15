module com.example.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.annotation;
    requires retrofit2;
    requires retrofit2.converter.jackson;
    requires com.fasterxml.jackson.databind;
    requires okhttp3;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.core;
    requires spring.context;
    requires spring.beans;
    requires org.slf4j;
    requires transitive org.controlsfx.controls;
    requires static lombok;
    requires java.xml;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires org.apache.commons.lang3;
    requires java.desktop;
    requires org.apache.commons.io;
    requires javafx.swing;
    requires java.sql;
    requires spring.websocket;
    requires spring.messaging;

    opens com.example.client to javafx.fxml, spring.core, javafx.graphics;
    opens com.example.client.endpoints to spring.core, spring.beans, spring.context;
    exports com.example.client;
    exports com.example.client.model;
    opens com.example.client.model to javafx.fxml;
    exports com.example.client.controller;
    opens com.example.client.controller to javafx.fxml, javafx.graphics, spring.core;
}


