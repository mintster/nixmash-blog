package com.nixmash.blog.jsoup;

import com.nixmash.blog.jsoup.base.JsoupHtmlParser;
import com.nixmash.blog.jsoup.dto.TestDTO;
import com.nixmash.blog.jsoup.parsers.TestDTOParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by daveburke on 5/21/16.
 */
@Configuration
public class JsoupTestConfig {

    @Bean
    public JsoupHtmlParser<TestDTO> testDTOParser() {
        return new TestDTOParser(TestDTO.class);
    }
}
