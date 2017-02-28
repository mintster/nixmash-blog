package com.nixmash.blog.solr;

import com.nixmash.blog.jpa.config.ApplicationConfig;
import com.nixmash.blog.solr.common.SolrUI;
import com.nixmash.blog.solr.config.SolrApplicationConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;

@SpringBootApplication
@Import(ApplicationConfig.class)
public class SolrLauncher {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(SolrApplicationConfig.class);
        ctx.refresh();
        System.out.println("Using Spring Framework Version: " + SpringVersion.getVersion());
        System.out.println("Solr Active Profile: " + ctx.getEnvironment().getActiveProfiles()[0]);
        SolrUI ui = ctx.getBean(SolrUI.class);
        if (doReIndex(args))
            ui.populate();
        else
            ui.init();
        ctx.close();
    }

    private static boolean doReIndex(String[] args) {
        PropertySource ps = new SimpleCommandLinePropertySource(args);
        if (ps.getProperty("reindex") == null)
            return false;
        else
            return Boolean.valueOf(ps.getProperty("reindex").toString());
    }

}
