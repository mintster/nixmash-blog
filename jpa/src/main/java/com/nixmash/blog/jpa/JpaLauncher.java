package com.nixmash.blog.jpa;

import com.nixmash.blog.jpa.components.JpaUI;
import com.nixmash.blog.jpa.config.ApplicationConfig;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.SpringVersion;

@EnableCaching
@SpringBootApplication
public class JpaLauncher {

	public static void main(String[] args) {

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(ApplicationConfig.class);
		ctx.refresh();
		System.out.println("Spring Framework Version: " + SpringVersion.getVersion());
		System.out.println("Spring Boot Version: " + SpringBootVersion.getVersion());
		JpaUI ui = ctx.getBean(JpaUI.class);
		ui.init();
		ctx.close();
	}

}
