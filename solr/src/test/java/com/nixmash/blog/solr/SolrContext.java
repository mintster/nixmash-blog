package com.nixmash.blog.solr;

import com.nixmash.blog.jpa.config.ApplicationConfig;
import com.nixmash.blog.jpa.enums.DataConfigProfile;
import com.nixmash.blog.solr.common.SolrSettings;
import com.nixmash.blog.solr.config.SolrApplicationConfig;
import com.nixmash.blog.solr.config.SolrTestConfig;
import com.nixmash.blog.solr.enums.SolrConfigProfile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SolrTestConfig.class, ApplicationConfig.class, SolrApplicationConfig.class})
@ActiveProfiles({ DataConfigProfile.H2, SolrConfigProfile.DEV })
public class SolrContext {

	@Autowired
	private SolrSettings solrSettings;

	@Test
	public void contextLoads() {
		assertNotNull(solrSettings.getSolrServerUrl());
	}

}
