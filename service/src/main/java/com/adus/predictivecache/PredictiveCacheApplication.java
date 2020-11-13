package com.adus.predictivecache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages = "com.adus.predictivecache")
public class PredictiveCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(PredictiveCacheApplication.class, args);
    }
}
