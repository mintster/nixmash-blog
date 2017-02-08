package com.nixmash.blog.solr.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@Configuration
@EnableConfigurationProperties
@EnableSolrRepositories(basePackages = "com.nixmash.blog.solr.repository",
        namedQueriesLocation = "classpath:named-queries.properties" )
@ComponentScan(basePackages = {"com.nixmash.blog.solr", "com.nixmash.blog.jpa"})
@Import({ EmbeddedSolrContext.class, HttpSolrContext.class })
@PropertySource("classpath:solr.properties")
public class SolrApplicationConfig {

}