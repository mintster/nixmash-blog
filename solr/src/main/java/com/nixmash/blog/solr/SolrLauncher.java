package com.nixmash.blog.solr;

import com.nixmash.blog.solr.common.SolrUI;
import com.nixmash.blog.solr.config.SolrApplicationConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.SpringVersion;

@SpringBootApplication
public class SolrLauncher {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
//		AnnotationConfigApplicationContext ctx = new
//				AnnotationConfigApplicationContext("com.nixmash.blog.solr",
//				"com.nixmash.blog.jpa");
		ctx.register(SolrApplicationConfig.class);
		ctx.refresh();
		System.out.println("Using Spring Framework Version: " + SpringVersion.getVersion());
		System.out.println("Solr Active Profile: " + ctx.getEnvironment().getActiveProfiles()[0]);
		SolrUI ui = ctx.getBean(SolrUI.class);
		ui.init();
		ctx.close();
	}
}
