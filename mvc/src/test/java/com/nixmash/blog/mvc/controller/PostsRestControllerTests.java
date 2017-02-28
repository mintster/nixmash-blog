package com.nixmash.blog.mvc.controller;

import com.nixmash.blog.jpa.common.ApplicationSettings;
import com.nixmash.blog.mvc.AbstractContext;
import com.nixmash.blog.solr.SolrTestUtils;
import com.nixmash.blog.solr.model.PostDoc;
import com.nixmash.blog.solr.service.PostDocService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
public class PostsRestControllerTests extends AbstractContext {

    @Autowired
    protected WebApplicationContext wac;

    private MockMvc mockMvc;

    @MockBean
    private PostDocService mockPostDocService;

    @Autowired
    private ApplicationSettings applicationSettings;

    private List<PostDoc> morelikethisDocs;
    private final Long MORELIKETHIS_POSTID = 1L;

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(wac)
                .apply(springSecurity())
                .build();

        when(mockPostDocService.getMoreLikeThis(MORELIKETHIS_POSTID)).thenReturn(SolrTestUtils.createPostList(3));
    }

    @Test
    public void getMoreLikeThis_MoreLikeThisDisplay_False() throws Exception {

        applicationSettings.setMoreLikeThisDisplay(false);
        MvcResult mvcResult = mockMvc.perform(get("/json/posts/post/mlt/" + MORELIKETHIS_POSTID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)).andReturn();

        assertEquals(mvcResult.getResponse().getContentAsString(), StringUtils.EMPTY);

    }

    @Test
    public void getMoreLikeThis_PostDocLessThanAppSetting() throws Exception {

        applicationSettings.setMoreLikeThisNum(1000);
        MvcResult mvcResult = mockMvc.perform(get("/json/posts/post/mlt/" + MORELIKETHIS_POSTID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)).andReturn();

        assertEquals(mvcResult.getResponse().getContentAsString(), StringUtils.EMPTY);

    }

    @Test
    public void getMoreLikeThis_MoreLikeThisDisplay_True() throws Exception {

        applicationSettings.setMoreLikeThisDisplay(true);
        MvcResult mvcResult = mockMvc.perform(get("/json/posts/post/mlt/" + MORELIKETHIS_POSTID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)).andReturn();

        assertThat(mvcResult.getResponse().getContentAsString(), containsString("<div"));

    }

    @Test
    public void getTags() throws Exception {
        mockMvc.perform(get("/json/posts/tags"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void keyValueJson() throws Exception {
        mockMvc.perform(get("/json/posts/map"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithAnonymousUser
    public void postsInHtmlContentType() throws Exception {
        mockMvc.perform(get("/json/posts/page/1"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    @Test
    @WithUserDetails(value = "keith", userDetailsServiceBeanName = "currentUserDetailsService")
    public void postPathsforAuthenticatedUsers() throws Exception {

        // liked posts
        mockMvc.perform(get("/json/posts/likes/3/page/2"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void postPathsforAnonymousUsers() throws Exception {

        // all posts
        mockMvc.perform(get("/json/posts/page/2"))
                .andExpect(status().isOk());

        // titles only
        mockMvc.perform(get("/json/posts/titles/page/2"))
                .andExpect(status().isOk());

        // by tag
        mockMvc.perform(get("/json/posts/tag/1/page/2"))
                .andExpect(status().isOk());

        // by tag titles
        mockMvc.perform(get("/json/posts/titles/tag/1/page/2"))
                .andExpect(status().isOk());

    }

    @Test
    @WithUserDetails(value = "erwin", userDetailsServiceBeanName = "currentUserDetailsService")
    public void newLikedPostReturnsPlusOne() throws Exception {
        // no pre-existing post likes for Erwin
        mockMvc.perform(get("/json/posts/post/like/3"))
                .andExpect(content().string("1"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithUserDetails(value = "keith", userDetailsServiceBeanName = "currentUserDetailsService")
    public void existingLikedPostClickReturnsMinusOne() throws Exception {
        // pre-existing postId 3 like for Keith
        mockMvc.perform(get("/json/posts/post/like/3"))
                .andExpect(content().string("-1"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
