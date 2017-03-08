package com.nixmash.blog.mvc.controller;

import com.nixmash.blog.jpa.common.ApplicationSettings;
import com.nixmash.blog.jpa.model.Post;
import com.nixmash.blog.jpa.service.PostService;
import com.nixmash.blog.mvc.AbstractContext;
import com.nixmash.blog.mvc.annotations.WithAdminUser;
import com.nixmash.blog.mvc.dto.JsonPostDTO;
import com.nixmash.blog.solr.model.PostDoc;
import com.nixmash.blog.solr.service.PostDocService;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletException;
import java.util.List;

import static com.nixmash.blog.mvc.controller.AdminPostsController.*;
import static com.nixmash.blog.mvc.security.SecurityRequestPostProcessors.csrf;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SuppressWarnings("unused")
@RunWith(SpringRunner.class)
@WithAdminUser
public class AdminSolrPostsControllerTests extends AbstractContext{

    // region Logger and Constants

    private static final Logger logger = LoggerFactory.getLogger(AdminSolrPostsControllerTests.class);

    private static final String POST_CONSTANT = "POST";
    private static final String TWITTER_SUMMARY = "SUMMARY";
    private static final String LINK_CONSTANT = "LINK";
    private static final String GOOD_URL = "http://nixmash.com/some-post/";

    // endregion

    // region  Variables

    private JacksonTester<JsonPostDTO> json;

    private MockMvc mvc;
    private String azTestFileName;

    // endregion

    // region Beans

    @Autowired
    private Environment environment;

    @Autowired
    private ApplicationSettings applicationSettings;

    @Autowired
    private SolrOperations solrOperations;

    @Autowired
    private PostService postService;

    @Autowired
    private PostDocService postDocService;

    @Autowired
    protected WebApplicationContext wac;

    // endregion

    // region Before / After

    @Before
    public void setup() throws ServletException {

//        ObjectMapper objectMapper = new ObjectMapper();
//        JacksonTester.initFields(this, objectMapper);

        mvc = webAppContextSetup(wac)
                .apply(springSecurity())
                .build();

        Query query = new SimpleQuery(new SimpleStringCriteria("doctype:post"));
        solrOperations.delete(query);
        solrOperations.commit();
        List<Post> posts = postService.getAllPublishedPosts();
        postDocService.addAllToIndex(posts);
    }

    @After
    public void tearDown() {
    }

    // endregion

    // region Solr


    @Test
    public void addNewPublishedPost_IncreasesPostAndPostDocSize() throws Exception {

        // Adding a Published Post increases the PostCount and PostDocCount by 1

        String TITLE = "addNewPublishedPost";
        String SOLR_TITLE = "title:addNewPublishedPost";

        int postStartCount = postService.getAllPosts().size();
        int postDocStartCount = postDocService.getAllPostDocuments().size();

        mvc.perform(addPostRequest(TITLE));

        int postEndCount = postService.getAllPosts().size();
        assertEquals(postStartCount + 1, postEndCount);

        int postDocEndCount = postDocService.getAllPostDocuments().size();
        assertEquals(postDocStartCount + 1, postDocEndCount);
        postDocService.removeFromIndex(SOLR_TITLE);

    }

    @Test
    public void addingUnPublishedPost_NoChangeInPostDocSize() throws Exception {

        // Adding a Published Post increases the PostCount and PostDocCount by 1

        String TITLE = "addingUnPublishedPost";
        String SOLR_TITLE = "title:addingUnPublishedPost";

        int postDocStartCount = postDocService.getAllPostDocuments().size();

        mvc.perform(addPostRequest(TITLE, false, false));

        int postDocEndCount = postDocService.getAllPostDocuments().size();
        assertEquals(postDocStartCount, postDocEndCount);
        postDocService.removeFromIndex(SOLR_TITLE);

    }

    @Test
    public void updatingPostUpdatesItsSolrPostDocument() throws Exception {

        List<PostDoc> postDocs = postDocService.getAllPostDocuments();
        int postDocCount= postDocs.size();

        Post post = postService.getPostById(1L);
        String originalTitle = post.getPostTitle();
        String newTitle = "updatingPostUpdatesItsSolrPostDocument";

        mvc.perform(post("/admin/posts/update")
                .param("postId", "1")
                .param("displayType", String.valueOf(post.getDisplayType()))
                .param("postContent", post.getPostContent())
                .param("postTitle", newTitle)
                .param("twitterCardType", TWITTER_SUMMARY)
                .param("tags", "removingTag1")
                .with(csrf()));

        // size of PostDocuments in Solr Index Unchanged
        assertEquals(postDocCount, postDocService.getAllPostDocuments().size());

        Post verifyPost = postService.getPostById(1L);
        assertEquals(verifyPost.getPostTitle(), newTitle);

        List<PostDoc> verifyPostDocs = postDocService.getPostsWithUserQuery(newTitle);
        assertEquals(verifyPostDocs.size(), 1);

    }

    @Test
    public void reindexPageLoads() throws Exception {
        RequestBuilder request = get("/admin/posts/solr/reindex").with(csrf());
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name(ADMIN_POSTS_REINDEX_VIEW));
    }

    @Test
    public void reindexResetsSolrPosts() throws Exception {
        RequestBuilder request = get("/admin/posts/solr/reindex")
                .param("reindex", "doit")
                .with(csrf());

        int originalPostDocCount = postDocService.getAllPostDocuments().size();
        postDocService.removeFromIndex(postDocService.getPostDocByPostId(1L));
       assertThat(postDocService.getAllPostDocuments().size(), is(lessThan(originalPostDocCount)));

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("hasPostDocCount"))
                .andExpect(view().name(ADMIN_POSTS_REINDEX_VIEW));

        assertEquals(postDocService.getAllPostDocuments().size(), originalPostDocCount);
    }

    // endregion

    // region Utility Methods


    private RequestBuilder addPostRequest(String s) {
        return addPostRequest(s, true, false);
    }

    private RequestBuilder addUnpublishedPostRequest(String s) {
        return addPostRequest(s, false, false);
    }

    private RequestBuilder addPostRequest(String s, Boolean isPublished, Boolean addSolrCategory) {
        return post("/admin/posts/add/post")
                .param("post", isPublished ? POST_PUBLISH : POST_DRAFT)
                .param("postTitle", "my title " + s)
                .param("hasPost", "true")
                .param("postLink", StringUtils.EMPTY)
                .param("postType", POST_CONSTANT)
                .param("twitterCardType", TWITTER_SUMMARY)
                .param("displayType", POST_CONSTANT)
                .param("postContent", "My Post Content must be longer so I don't trigger a new contraint addition!")
                .param("isPublished", isPublished.toString())
                .param("tags", String.format("req%s, req%s%s", s, s, 1))
                .param("categoryId", addSolrCategory ? "3" : "1")
                .with(csrf());
    }

    private RequestBuilder solrCategoryRequest(String s) {
        return addPostRequest(s, true, true);
    }

    // endregion

}
