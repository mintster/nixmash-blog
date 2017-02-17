package com.nixmash.blog.solr.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("file:${solr.properties.file.path}${solr.properties.file.prefix}.properties")
@ConfigurationProperties(prefix="solr")
public class SolrSettings {

	private String solrServerUrl;
	private String solrEmbeddedPath;
	private String solrCoreName;

	public String getSolrServerUrl() {
		return solrServerUrl;
	}

	public void setSolrServerUrl(String solrServerUrl) {
		this.solrServerUrl = solrServerUrl;
	}

	public String getSolrEmbeddedPath() {
		return solrEmbeddedPath;
	}

	public void setSolrEmbeddedPath(String solrEmbeddedPath) {
		this.solrEmbeddedPath = solrEmbeddedPath;
	}

	public String getSolrCoreName() {
		return solrCoreName;
	}

	public void setSolrCoreName(String solrCoreName) {
		this.solrCoreName = solrCoreName;
	}
}
