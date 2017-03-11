package com.nixmash.blog.jsoup.service;

import com.nixmash.blog.jpa.common.ApplicationSettings;
import com.nixmash.blog.jpa.dto.PostDTO;
import com.nixmash.blog.jpa.enums.PostDisplayType;
import com.nixmash.blog.jpa.enums.TwitterCardType;
import com.nixmash.blog.jpa.model.Post;
import com.nixmash.blog.jpa.model.PostImage;
import com.nixmash.blog.jpa.model.PostMeta;
import com.nixmash.blog.jpa.repository.PostMetaRepository;
import com.nixmash.blog.jpa.service.PostService;
import com.nixmash.blog.jpa.utils.PostUtils;
import com.nixmash.blog.jsoup.base.JsoupHtmlParser;
import com.nixmash.blog.jsoup.dto.JsoupPostDTO;
import com.nixmash.blog.jsoup.dto.PagePreviewDTO;
import com.nixmash.blog.jsoup.utils.JsoupUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Service
@Transactional
public class JsoupServiceImpl implements JsoupService {

    private static final Logger logger =
            LoggerFactory.getLogger(JsoupServiceImpl.class);

    @Value("${jsoup.connect.useragent}")
    private String userAgent;

    private final ApplicationSettings applicationSettings;
    private final PostMetaRepository postMetaRepository;
    private final PostService postService;

    public JsoupServiceImpl(ApplicationSettings applicationSettings, PostMetaRepository postMetaRepository, PostService postService) {
        this.applicationSettings = applicationSettings;
        this.postMetaRepository = postMetaRepository;
        this.postService = postService;
    }

    // region PagePreview

    @Autowired
    @Qualifier("pagePreviewParser")
    private JsoupHtmlParser<PagePreviewDTO> pagePreviewParser;

    @Autowired
    @Qualifier("jsoupPostParser")
    private JsoupHtmlParser<JsoupPostDTO> jsoupPostParser;


    @Override
    public PagePreviewDTO getPagePreview(String url) {
        PagePreviewDTO pagePreviewDTO = null;
        Document doc;
        Boolean tryWithoutCertValidation = false;
        try {
            doc = getDocument(url, true);
            pagePreviewDTO = pagePreviewParser.parse(doc);
        } catch (IOException e) {
            logger.info(
                    String.format("Jsoup IOException [validCert = TRUE]  url [%s] : %s", url, e.getMessage()));
            tryWithoutCertValidation = true;
        }
        if (tryWithoutCertValidation) {
            try {
                doc = getDocument(url, false);
                pagePreviewDTO = pagePreviewParser.parse(doc);
            } catch (IOException e) {
                logger.info(
                        String.format("Jsoup IOException [validCert = FALSE]  url [%s] : %s", url, e.getMessage()));
                return null;
            }
        }
        return pagePreviewDTO;
    }

    private Document getDocument(String url, Boolean validateCert)
            throws IOException {
        return Jsoup.connect(url)
                .userAgent(userAgent)
                .timeout(12000)
                .referrer("http://www.google.com")
                .followRedirects(true)
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .validateTLSCertificates(validateCert)
                .get();
    }

    // endregion

    // region TwitterCards

    @Override
    public void updateAllPostMeta(List<Post> posts) {
        for (Post post : posts) {
//            try {
//                Post updated = postService.getPostById(post.getPostId());
                post.setPostMeta(updatePostMeta(PostUtils.postToPostDTO(post)));
//            } catch (PostNotFoundException e) {
//                e.printStackTrace();
//            }
        }
    }

    @Override
    public PostMeta createPostMeta(PostDTO postDTO) {
        PostMeta postMeta = buildPostMetaToSave(postDTO);
        return postMetaRepository.save(postMeta);
    }

    @Override
    public PostMeta updatePostMeta(PostDTO postDTO) {

        PostMeta postMeta = buildPostMetaToSave(postDTO);
        PostMeta updated = postMetaRepository.findByPostId(postDTO.getPostId());

        updated.update(postMeta.getTwitterImage(),
                postMeta.getTwitterCreator(),
                postMeta.getTwitterDescription(),
                postMeta.getTwitterCardType());
        return updated;
    }

    private PostMeta buildPostMetaToSave(PostDTO postDTO) {

        if (!postDTO.getTwitterCardType().equals(TwitterCardType.NONE)) {
            String twitterCreator = applicationSettings.getTwitterCreator();
            JsoupPostDTO jsoupPostDTO = getJsoupPostDTO(postDTO);
            String twitterImage = jsoupPostDTO.getTwitterImagePath();
            String twitterDescription = jsoupPostDTO.getTwitterDescription();

            return PostMeta.getUpdated(postDTO.getTwitterCardType(),
                    twitterImage,
                    twitterDescription)
                    .twitterCreator(twitterCreator)
                    .postId(postDTO.getPostId())
                    .build();
        } else
            return PostMeta
                    .getEmpty(postDTO.getPostId(), TwitterCardType.NONE)
                    .build();
    }

    private JsoupPostDTO getJsoupPostDTO(PostDTO postDTO) {

        Document doc = Jsoup.parse(postDTO.getPostContent());
        JsoupPostDTO jsoupPostDTO = jsoupPostParser.parse(doc);
        if (jsoupPostDTO.hasImages()) {
            String imageUrl = jsoupPostDTO.getImagesInContent().get(0).getSrc();
            try {
                jsoupPostDTO.setTwitterImagePath(JsoupUtil.removeBaseUri(imageUrl));
            } catch (MalformedURLException e) {
                jsoupPostDTO.setTwitterImagePath(applicationSettings.getTwitterImage());
            }
        } else
            jsoupPostDTO.setTwitterImagePath(getTwitterImage(postDTO));

        jsoupPostDTO.setTwitterDescription(JsoupUtil.getTwitterDescription(jsoupPostDTO.getBodyText()));
        return jsoupPostDTO;
    }

    private String getTwitterImage(PostDTO postDTO) {
        String twitterImage = applicationSettings.getTwitterImage();
        PostDisplayType postDisplayType = postDTO.getDisplayType();
        if (postDisplayType == PostDisplayType.MULTIPHOTO_POST ||
                postDisplayType == PostDisplayType.SINGLEPHOTO_POST) {
            List<PostImage> postImages = postService.getPostImages(postDTO.getPostId());
            if (!postImages.isEmpty()) {
                PostImage postImage = postImages.get(0);
                twitterImage = postImage.getUrl() + postImage.getNewFilename();
            }
        }
        return twitterImage;
    }

    // endregion

}




