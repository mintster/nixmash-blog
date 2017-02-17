package com.nixmash.blog.solr.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@Configuration
@EnableConfigurationProperties
@EnableSolrRepositories(basePackages = "com.nixmash.blog.solr.repository",
        namedQueriesLocation = "classpath:named-queries.properties" )
@ComponentScan(basePackages = {"com.nixmash.blog.solr", "com.nixmash.blog.jpa"})
@Import({ EmbeddedSolrContext.class, HttpSolrContext.class })
@PropertySources({
        @PropertySource("classpath:solr.properties"),
        @PropertySource("file:${jpa.resources.file.path}/application.properties")
})
public class SolrApplicationConfig {

}
