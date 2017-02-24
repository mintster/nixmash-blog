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
import org.springframework.web.context.WebApplicationContext;

import static com.nixmash.blog.mvc.controller.PermaPostsController.POSTS_PERMALINK_VIEW;
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

}
