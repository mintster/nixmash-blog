package com.nixmash.blog.mvc.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.List;

@Configuration
@EnableAutoConfiguration
@PropertySource("classpath:mvc.properties")
public class WebConfig extends WebMvcConfigurerAdapter {

    private static final String USE_CODE_AS_DEFAULT_MESSAGE = "use.code.as.default.message";

    @Autowired
    private Environment environment;

    @Value("${nixmash.mode.enabled}")
    private Boolean nixmashModeEnabled;

    // region Template Resolvers

    @Bean
    public ITemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        if (nixmashModeEnabled)
            resolver.setPrefix("classpath:/nixmash/");
        else
            resolver.setPrefix("classpath:/templates/");

        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setCacheable(false);
        resolver.setOrder(0);
        return resolver;
    }

    @Bean
    public ITemplateResolver adminTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("classpath:/admin/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setCacheable(false);
        resolver.setOrder(1);
        return resolver;
    }

    // endregion

    // region Resources

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (nixmashModeEnabled) {
            registry.addResourceHandler("/**")
                    .addResourceLocations("classpath:/common/", "classpath:/private/");
        } else {
            registry.addResourceHandler("/**")
                    .addResourceLocations("classpath:/common/", "classpath:/static/");
        }
    }

    // endregion

    // region Messages

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new JavaTimeModule());
        converter.setObjectMapper(objectMapper);
        converters.add(converter);
        super.configureMessageConverters(converters);
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource msgsource = new ResourceBundleMessageSource();
        if (nixmashModeEnabled)
            msgsource.setBasename("nixmash");
        else
            msgsource.setBasename("messages");

        msgsource.setUseCodeAsDefaultMessage(
                Boolean.parseBoolean(environment.getRequiredProperty(USE_CODE_AS_DEFAULT_MESSAGE)));
        return msgsource;
    }

    @Bean(name = "validator")
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    @Override
    public Validator getValidator() {
        return validator();
    }

    // endregion

}
