package com.nixmash.blog.jsoup.parsers;

import com.nixmash.blog.jsoup.base.JsoupHtmlParser;
import com.nixmash.blog.jsoup.dto.JsoupPostDTO;

public class JsoupPostParser extends JsoupHtmlParser<JsoupPostDTO> {

    public JsoupPostParser(Class<JsoupPostDTO> classModel) {
        super(classModel);
    }

}