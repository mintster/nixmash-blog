package com.nixmash.blog.batch.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by daveburke on 11/12/16.
 */
@Configuration
@EnableBatchProcessing
@EnableScheduling
@ComponentScan(basePackages = {
        "com.nixmash.blog.batch", "com.nixmash.blog.jpa"
})
@PropertySources({
        @PropertySource("classpath:/batch.properties"),
        @PropertySource("file:/home/daveburke/web/nixmash/jobs.properties")
})
public class ApplicationConfiguration {
}
