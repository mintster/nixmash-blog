package com.nixmash.blog.mvc.controller;

import com.nixmash.blog.jpa.model.GitHubStats;
import com.nixmash.blog.mvc.AbstractContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static com.nixmash.blog.mvc.controller.GeneralController.HOME_VIEW;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isA;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@RunWith(SpringJUnit4ClassRunner.class)
public class GeneralControllerTests extends AbstractContext {

    // region Beans

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    // endregion

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void homePageTest() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("gitHubStats", isA(GitHubStats.class)))
                .andExpect(view().name(HOME_VIEW));
    }

    @Test
    public void resourceNotFoundExceptionTest() throws Exception {
        mockMvc.perform(get("/badurl"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void retrieveRobotsTxtFile() throws Exception {
        mockMvc.perform(get("/robots.txt"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().string(containsString("Disallow")));

    }
}
