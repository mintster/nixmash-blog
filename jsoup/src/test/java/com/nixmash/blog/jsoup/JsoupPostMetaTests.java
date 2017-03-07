package com.nixmash.blog.jsoup;

import com.nixmash.blog.jsoup.base.JsoupHtmlParser;
import com.nixmash.blog.jsoup.base.JsoupImage;
import com.nixmash.blog.jsoup.dto.JsoupPostDTO;
import com.nixmash.blog.jsoup.utils.JsoupTestUtil;
import com.nixmash.blog.jsoup.utils.JsoupUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
public class JsoupPostMetaTests extends JsoupContext {

    private Document docWithImages;
    private Document docNoImages;
    private Document docShortBody;

    private JsoupPostDTO jsoupPostDTOWithImages;
    private JsoupPostDTO jsoupPostDTONoImages;
    private JsoupPostDTO jsoupPostDTOShortBody;

    @Autowired
    @Qualifier("jsoupPostParser")
    JsoupHtmlParser<JsoupPostDTO> jsoupPostParser;

    @Before
    public void setup() throws IOException {

        File withImagesIn = JsoupTestUtil.getFile("/html/testPostBody.html");
        docWithImages = Jsoup.parse(withImagesIn, null, "http://example.com");
        jsoupPostDTOWithImages = jsoupPostParser.parse(docWithImages);

        File noImagesIn = JsoupTestUtil.getFile("/html/testNoImgBody.html");
        docNoImages = Jsoup.parse(noImagesIn, null, "http://example.com");
        jsoupPostDTONoImages = jsoupPostParser.parse(docNoImages);

        File shortBodyIn = JsoupTestUtil.getFile("/html/testShortPostBody.html");
        docShortBody = Jsoup.parse(shortBodyIn, null, "http://example.com");
        jsoupPostDTOShortBody = jsoupPostParser.parse(docShortBody);

    }

    @Test
    public void extractFirstImageFromContent() throws IOException {

        // First Src URL in  /html/testPostBody.html = http://nixmash.com/x/blog/2017/dd0120a.png

        List<JsoupImage> images = jsoupPostDTOWithImages.getImagesInContent();
        assertNotNull(images);
        JsoupImage img = jsoupPostDTOWithImages.getImagesInContent().get(0);
        assertThat(img.getSrc()).isEqualTo("http://nixmash.com/x/blog/2017/dd0120a.png");
    }

    @Test
    public void processContentWithNoImage() throws IOException {
        List<JsoupImage> images = jsoupPostDTONoImages.getImagesInContent();
        assertThat(images.size()).isEqualTo(0);
        assertThat(images).isEmpty();
    }

    @Test
    public void stripBaseUrlFromImageUrl() throws MalformedURLException {
        String imageUrl;

        // Full Url returns correct path
        imageUrl = "http://nixmash.com/x/blog/2017/dd0120a.png";
        assertThat(JsoupUtil.removeBaseUri(imageUrl)).isEqualTo("/x/blog/2017/dd0120a.png");

        // path returns path
        imageUrl = "/x/blog/2017/dd0120a.png";
        assertThat(JsoupUtil.removeBaseUri(imageUrl)).isEqualTo("/x/blog/2017/dd0120a.png");

    }

    @Test
    public void getPostDescriptionTest() {
        assertNotNull(jsoupPostDTOWithImages.getBodyText());
        assertNotNull(jsoupPostDTOShortBody.getBodyText());
    }

}
