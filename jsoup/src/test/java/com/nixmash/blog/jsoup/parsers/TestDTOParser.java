package com.nixmash.blog.jsoup.parsers;

import com.nixmash.blog.jsoup.base.JsoupHtmlParser;
import com.nixmash.blog.jsoup.dto.TestDTO;

public class TestDTOParser extends JsoupHtmlParser<TestDTO> {

    public TestDTOParser(Class<TestDTO> classModel) {
        super(classModel);
    }

}