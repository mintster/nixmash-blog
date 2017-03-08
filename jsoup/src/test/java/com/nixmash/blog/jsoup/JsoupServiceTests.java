package com.nixmash.blog.jsoup;


import com.nixmash.blog.jpa.common.ApplicationSettings;
import com.nixmash.blog.jpa.service.PostService;
import com.nixmash.blog.jsoup.dto.PagePreviewDTO;
import com.nixmash.blog.jsoup.service.JsoupService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
public class JsoupServiceTests extends JsoupContext {

    @Autowired
    private JsoupService jsoupService;

    @Autowired
    private PostService postService;

    @Autowired
    private ApplicationSettings applicationSettings;

    private static final String REPO_URL = "https://github.com/mintster/spring-data";

// region PagePreview Tests

    @Test
    public void throwIOExceptionWithBadUrl() throws IOException {
        PagePreviewDTO pagePreviewDTO = jsoupService.getPagePreview("http://bad.url");
        assert (pagePreviewDTO == null);
    }

    @Test
    public void retrievePagePreviewDTOWithValidUrl() {
        assert (jsoupService.getPagePreview(REPO_URL) != null);
    }

    // endregion
}
