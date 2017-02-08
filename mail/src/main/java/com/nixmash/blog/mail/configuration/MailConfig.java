package com.nixmash.blog.mail.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/mail.properties")
public class MailConfig {

}



