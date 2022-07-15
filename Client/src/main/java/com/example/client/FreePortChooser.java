package com.example.client;


import org.springframework.util.SocketUtils;
import org.springframework.util.StringUtils;
//https://www.javacodemonk.com/setting-a-random-port-in-spring-boot-application-at-startup-87022e01
public class FreePortChooser {

    public static void setRandomPort(int minPort, int maxPort) {
        try {
            String userDefinedPort = System.getProperty("server.port", System.getenv("SERVER_PORT"));
            if(StringUtils.isEmpty(userDefinedPort)) {
                int port = SocketUtils.findAvailableTcpPort(minPort, maxPort);
                System.setProperty("server.port", String.valueOf(port));
                System.out.println("Random Server Port is set to "  + port);
            }
        } catch( IllegalStateException e) {
            System.out.println("No port available in range 9000-9100. Default embedded server configuration will be used.");
        }
    }
}
