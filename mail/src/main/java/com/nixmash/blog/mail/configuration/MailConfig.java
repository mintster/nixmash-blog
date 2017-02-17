package com.nixmash.blog.mail.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Configuration
@PropertySource("classpath:/mail.properties")
public class MailConfig {

    @Value("${nixmash.mode.enabled}")
    private Boolean nixmashModeEnabled;

    @Bean
    public FreeMarkerViewResolver freemarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setOrder(3);
        return resolver;
    }

    @Bean
    public FreeMarkerConfigurer freemarkerConfig() {
        FreeMarkerConfigurer result = new FreeMarkerConfigurer();
        if (nixmashModeEnabled)
            result.setTemplateLoaderPath("classpath:/nixmarker/");
        else
            result.setTemplateLoaderPath("classpath:/freemarker/");
        return result;

    }

    @Bean
    public MessageSource mailMessageSource() {
        ResourceBundleMessageSource msgsource = new ResourceBundleMessageSource();
        if (nixmashModeEnabled)
            msgsource.setBasename("mail-nixmash");
        else
            msgsource.setBasename("mail-messages");
        return msgsource;
    }
}



