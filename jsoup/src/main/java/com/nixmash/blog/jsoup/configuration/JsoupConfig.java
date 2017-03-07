package com.nixmash.blog.jsoup.configuration;

import com.nixmash.blog.jpa.config.ApplicationConfig;
import com.nixmash.blog.jsoup.dto.PagePreviewDTO;
import com.nixmash.blog.jsoup.base.JsoupHtmlParser;
import com.nixmash.blog.jsoup.dto.JsoupPostDTO;
import com.nixmash.blog.jsoup.parsers.PagePreviewParser;
import com.nixmash.blog.jsoup.parsers.JsoupPostParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/jsoup.properties")
@Import({ApplicationConfig.class})
public class JsoupConfig {

    @Bean
    public JsoupHtmlParser<PagePreviewDTO> pagePreviewParser() {
        return new PagePreviewParser(PagePreviewDTO.class);
    }

    @Bean
    public JsoupHtmlParser<JsoupPostDTO> jsoupPostParser() {
        return new JsoupPostParser(JsoupPostDTO.class);
    }
}




