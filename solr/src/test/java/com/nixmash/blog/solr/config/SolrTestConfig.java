package com.nixmash.blog.solr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * Created by daveburke on 2/17/17.
 */
@Configuration
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource("file:${project.file.path.root}jpa/src/main/resources/application.properties")
})
public class SolrTestConfig {
}
