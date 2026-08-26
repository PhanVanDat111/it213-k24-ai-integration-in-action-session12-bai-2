package com.rikkei.mcp;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LogisticsMcpServerApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(LogisticsMcpServerApplication.class);
        // TAT HOAN TOAN BANNER KHI KHOI DONG DE TRAI o NHIEM STDIO
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}