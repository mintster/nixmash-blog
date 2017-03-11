package com.nixmash.blog.mail;

import com.nixmash.blog.jpa.exceptions.PostNotFoundException;
import com.nixmash.blog.jpa.model.Post;
import com.nixmash.blog.jpa.model.PostMeta;
import com.nixmash.blog.jpa.model.User;
import com.nixmash.blog.jpa.service.PostService;
import com.nixmash.blog.jpa.service.UserService;
import com.nixmash.blog.jpa.utils.PostTestUtils;
import com.nixmash.blog.mail.components.MailUI;
import com.nixmash.blog.mail.service.FmService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
public class FmServiceTests extends MailContext {

    private User user;
    private Post post;
    private Post link;
    private String postTitle;
    private String linkTitle;
    private String siteName;

    @Autowired
    Environment environment;

    @Autowired
    FmService fmService;

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    MailUI mailUI;

    @Before
    public void setup() throws PostNotFoundException {
        user = userService.getUserByUsername("erwin");                            // User:  Erwin Lapote
        post = postService.getPostById(1L);                                                         // Post Title: Freestanding Note Post
        postTitle = post.getPostTitle();
        link = postService.getPostById(3L);                                                           // Link Post Type (no image): Jsoup Parsing and Traversing Document and URL - JAVATIPS.INFO
        linkTitle = link.getPostTitle();
        siteName = mailUI.getMessage("mail.site.name");                         // NixMash Spring
    }

    // region Test Template

    @Test
    public void testTemplate() {
        String result = fmService.displayTestTemplate(user);
        assertThat(result, containsString(siteName));
    }

    // endregion

    // region Twitter Meta Tags

    @Test
    public void twitterTemplate() {
        String result = fmService.getTwitterTemplate(PostTestUtils.createPostMeta());
        assertThat(result, containsString("@testblogger"));
    }

    @Test
    public void twitterSummaryTemplate() throws PostNotFoundException {
        Post post = postService.getPostById(1L);
        PostMeta postMeta = postService.buildTwitterMetaTagsForDisplay(post);
        String result = fmService.getTwitterTemplate(postMeta);
        assertThat(result, containsString("summary"));
    }


    @Test
    public void twitterSummaryLargeImageTemplate() throws PostNotFoundException {
        // H2 Post 10L SolrRama is SUMMARY_LARGE_IMAGE
        Post post = postService.getPostById(10L);
        PostMeta postMeta = postService.buildTwitterMetaTagsForDisplay(post);
        String result = fmService.getTwitterTemplate(postMeta);
        assertThat(result, containsString("summary_large_image"));
    }

    @Test
    public void noneTwitterTypeTemplateIsNull() throws PostNotFoundException {
        // H2 POST 9L is twitterCardType=NONE
        // postService.buildTwitterMetaTagsForDisplay(post) returns NULL PostMeta object
        Post post = postService.getPostById(9L);
        PostMeta postMeta = postService.buildTwitterMetaTagsForDisplay(post);
        assertNull(postMeta);
    }

    // endregion

    // region Rss Content Tests

    @Test
    public void rssPostDisplayTypeContentTest() throws  PostNotFoundException {
        // H2 POST 10L 'solr-rama' normal POST PostDisplayType
        Post post = postService.getPost("solr-rama");
        String result = fmService.createRssPostContent(post);
        assertThat(result, containsString("<strong>This is a post</strong>"));
    }

    @Test
    public void rssSinglePhotoPostDisplayTypeContentTest() throws  PostNotFoundException {
        // H2 POST 11L 'singlephoto-post' SINGLEPHOTO_POST PostDisplayType
        Post post = postService.getPost("singlephoto-post");
        String result = fmService.createRssPostContent(post);
        assertThat(result, containsString("src=\"http://"));
    }

    @Test
    public void rssMultiPhotoPostDisplayTypeContentTest() throws  PostNotFoundException {
        // H2 POST 12L 'multiphoto-post' MULTIPHOTO_POST PostDisplayType
        Post post = postService.getPost("multiphoto-post");
        String result = fmService.createRssPostContent(post);
        assertThat(result, containsString("src=\"http://"));
    }

    // endregion

    // region Posts

    @Test

    public void noLikesTemplate() {
        String result = fmService.getNoLikesMessage();
        assertThat(result, containsString("No Liked Posts Selected"));
    }

    @Test
    public void flashcardPostTemplate() {
        String result = fmService.createPostHtml(post, "flashcard_post");
        assertThat(result, containsString(postTitle));
    }

    @Test
    public void postTemplate() {
        String result = fmService.createPostHtml(post);
        assertThat(result, containsString(postTitle));
    }

    @Test
    public void titlePostTemplate() {
        String result = fmService.createPostHtml(post, "title");
        assertThat(result, containsString(postTitle));
    }

    @Test
    public void multiphotoPostTemplate() {
        String result = fmService.createPostHtml(post, "multiphoto_post");
        assertThat(result, containsString(postTitle));
    }

    @Test
    public void singlephotoPostTemplate() {
        String result = fmService.createPostHtml(post, "singlephoto_post");
        assertThat(result, containsString(postTitle));
    }

    // endregion

    // region Posts A-to-Z

    @Test
    public void postAZTemplatePopulated() throws Exception {
        String result = fmService.createPostAtoZs();
        assertThat(result, containsString("back-to-top"));
    }

    // endregion

    // region LInk Templates

    @Test
    public void linkTemplate() {
        String result = fmService.createPostHtml(link, "link");
        assertThat(result, containsString(linkTitle));
    }

    @Test
    public void linkSummaryTemplate() {
        String result = fmService.createPostHtml(link, "link_summary");
        assertThat(result, containsString(linkTitle));
    }

    @Test
    public void linkFeatureTemplate() {
        String result = fmService.createPostHtml(link, "link_feature");
        assertThat(result, containsString(linkTitle));
    }

    // endregion

    // region Utility Templates

    @Test
    public void robotsTxtTemplate() {
        String result = fmService.getRobotsTxt();
        assertThat(result, containsString("User-agent"));
    }

    @Test
    public void fileUploadedTemplate() {
        String result = fmService.getFileUploadedScript();
        assertThat(result, containsString("template-download"));
    }

    @Test
    public void fileUploadingTemplate() {
        String result = fmService.getFileUploadingScript();
        assertThat(result, containsString("template-upload"));
    }

    // endregion

}
