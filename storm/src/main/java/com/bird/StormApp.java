package com.bird;

import com.bird.utils.SpringContextUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author 李璞
 * @Date 2022/1/14 13:16
 * @Description
 */
@SpringBootApplication
public class StormApp {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(StormApp.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run();
    }
}
