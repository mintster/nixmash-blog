package com.nixmash.blog.mvc.controller;

import com.nixmash.blog.jpa.exceptions.PostNotFoundException;
import com.nixmash.blog.jpa.service.PostService;
import com.nixmash.blog.mvc.AbstractContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static com.nixmash.blog.mvc.controller.PermaPostsController.POSTS_PERMALINK_VIEW;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by daveburke on 2/22/17.
 */
@RunWith(SpringRunner.class)
public class PermaPostsControllerTests extends AbstractContext {

    private MockMvc mockMvc;
    private static final Logger logger = LoggerFactory.getLogger(PermaPostsControllerTests.class);


    @Autowired
    PostService postService;

    @Autowired
    WebApplicationContext wac;

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void goodCategoryPermaload() throws Exception {
        mockMvc.perform(get("/java/javascript-bootstrap"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/javascript-bootstrap"));
    }

    @Test
    public void badCategoryPermaload() throws Exception {
        mockMvc.perform(get("/badcategory/javascript-bootstrap"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void oldCategoryThrowsException() throws Exception {
            mockMvc.perform(get("/ruby-on-rails/javascript-bootstrap"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("errors/category"));
    }

    @Test
    public void postPermaload() throws Exception {
        mockMvc.perform(get("/post/javascript-bootstrap"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(view().name(POSTS_PERMALINK_VIEW));
    }

    @Test(expected = PostNotFoundException.class)
    public void notFoundPostName_ThrowsPostNotFoundException() throws Exception {
        String badName = "bad-name";
        when(postService.getPost(badName))
                .thenThrow(new PostNotFoundException());

        mockMvc.perform(get("/post/" + badName))
                .andExpect(status().isOk())
                .andExpect(view().name("errors/custom"));
    }

    @Test
    public void summaryLargeImageTwitterCardMetaDataExists() throws Exception {
        // H2 PostId 10L has SUMMARY_LARGE_IMAGE as TwitterCardType
        MvcResult mvcResult = this.mockMvc.perform(get("/post/solr-rama"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString(),
                containsString("<meta name=\"twitter:card\" content=\"summary_large_image\" />"));
    }

    @Test
    public void summaryTwitterCardMetaDataExists() throws Exception {
        // H2 PostId 1L has SUMMARY as TwitterCardType
        MvcResult mvcResult = this.mockMvc.perform(get("/post/javascript-bootstrap"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString(),
                containsString("<meta name=\"twitter:card\" content=\"summary\" />"));
    }

    @Test
    public void noMetaDataForTwitterCardTypeNone() throws Exception {
        // H2 PostId 9L has NONE as TwitterCardType
        MvcResult mvcResult = this.mockMvc.perform(get("/post/1000-ways-to-title-something"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString(),not(containsString("twitter:card")));
    }

}
