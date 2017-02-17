package com.nixmash.blog.mail;

import com.nixmash.blog.mail.components.MailDemo;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class MailLauncher {

	public static void main(String[] args) {

		ApplicationContext ctx = new
				AnnotationConfigApplicationContext("com.nixmash.blog.mail", "com.nixmash.blog.jpa");
		MailDemo demo = ctx.getBean(MailDemo.class);
		demo.init();
		((ConfigurableApplicationContext) ctx).close();
	}

}
