package com.nixmash.blog.jpa.config;

import com.nixmash.blog.jpa.model.auditors.CurrentTimeDateTimeService;
import com.nixmash.blog.jpa.model.auditors.DateTimeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "com.nixmash.blog.jpa")
@PropertySource("classpath:/application.properties")
public class ApplicationConfig {

    @Bean
    DateTimeService currentTimeDateTimeService() {
        return new CurrentTimeDateTimeService();
    }
}
