package com.nixmash.blog.solr.config;

import com.nixmash.blog.solr.common.SolrSettings;
import com.nixmash.blog.solr.repository.simple.SimpleProductRepositoryImpl;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.solr.core.SolrTemplate;

import java.nio.file.FileSystems;
import java.nio.file.Path;

@Configuration
@Profile("dev")
public class EmbeddedSolrContext {

	@Autowired
	private SolrSettings solrSettings;

	@Bean(name = "solrClient")
	public EmbeddedSolrServer solrServerFactoryBean() {
		String solrCoreName = solrSettings.getSolrCoreName();
		String solrHome = solrSettings.getSolrEmbeddedPath();
		Path path = FileSystems.getDefault().getPath(solrHome);
		CoreContainer container = CoreContainer.createAndLoad(path);
		return new EmbeddedSolrServer(container, solrCoreName);
	}

	@Bean
	public SolrTemplate solrTemplate() throws Exception {
		String solrCoreName = solrSettings.getSolrCoreName();
		SolrTemplate solrTemplate = new SolrTemplate(solrServerFactoryBean());
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
