package com.nixmash.blog.jsoup.service;

import com.nixmash.blog.jsoup.dto.PagePreviewDTO;

/**
 * Created by daveburke on 5/29/16.
 */
public interface JsoupService {
    PagePreviewDTO getPagePreview(String url);
}
