package com.nixmash.blog.solr.config;

import com.nixmash.blog.solr.common.SolrSettings;
import com.nixmash.blog.solr.repository.simple.SimpleProductRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.server.support.HttpSolrClientFactoryBean;

import javax.annotation.Resource;

@Configuration
@Profile("prod")
public class HttpSolrContext {

    @Resource
    private Environment environment;

    @Autowired
    private SolrSettings solrSettings;

    @Bean(name = "solrClient")
    public HttpSolrClientFactoryBean solrClient() {
        HttpSolrClientFactoryBean factory = new HttpSolrClientFactoryBean();
        factory.setUrl(solrSettings.getSolrServerUrl());
        return factory;
    }

    @Bean
    public SolrTemplate solrTemplate() {
        String solrCoreName = solrSettings.getSolrCoreName();
        SolrTemplate solrTemplate = new SolrTemplate(solrClient());
        solrTemplate.setSolrCore(solrCoreName);
        return solrTemplate;
    }

    @Bean
    public SimpleProductRepositoryImpl simpleProductRepository() throws Exception {
        SimpleProductRepositoryImpl simpleRepository = new SimpleProductRepositoryImpl();
        simpleRepository.setSolrOperations(solrTemplate());
        return simpleRepository;
    }
}
