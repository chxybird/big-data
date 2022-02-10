package com.bird;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author 李璞
 * @Date 2022/1/26 16:27
 * @Description
 */
@SpringBootApplication
public class SparkApp {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SparkApp.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run();
    }
}
