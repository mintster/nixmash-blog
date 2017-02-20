package com.nixmash.blog.mvc.controller;

import com.nixmash.blog.jpa.common.ApplicationSettings;
import com.nixmash.blog.jpa.common.SiteOptions;
import com.nixmash.blog.jpa.model.CurrentUser;
import com.nixmash.blog.mvc.AbstractContext;
import com.nixmash.blog.mvc.annotations.WithAdminUser;
import com.nixmash.blog.mvc.components.WebUI;
import com.nixmash.blog.mvc.security.CurrentUserDetailsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.nixmash.blog.mvc.security.SecurityRequestPostProcessors.csrf;
import static com.nixmash.blog.mvc.security.SecurityRequestPostProcessors.user;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SuppressWarnings("ALL")
@RunWith(SpringRunner.class)
public class GlobalControllerTests extends AbstractContext {

    private static final String TRACKING_ID = "UA-ABCXXX-20";

    // region Test Users

    @Autowired
    private CurrentUserDetailsService currentUserDetailsService;

    private CurrentUser keith;
    private CurrentUser erwin;

    // endregion

    private MockMvc mockMvc;

    @MockBean
    private SiteOptions siteOptions;

    private GlobalController globalController;
    private WebUI webUI;
    private ApplicationSettings applicationSettings;

    @Before
    public void setup() {
        when(siteOptions.getAddGoogleAnalytics()).thenReturn(true);
        when(siteOptions.getGoogleAnalyticsTrackingId()).thenReturn(TRACKING_ID);

        keith = currentUserDetailsService.loadUserByUsername("keith");
        erwin = currentUserDetailsService.loadUserByUsername("user");

        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        globalController = new GlobalController(siteOptions, webUI, applicationSettings);
    }

    @Test
    public void displayGoogleAnalyticsTest() throws Exception {

        // Keith is member of ROLE_POSTS so should not display Analytics
        assertFalse(globalController.displayGoogleAnalytics(getAuthenticationToken(keith)));

        // Erwin is member of ROLE_USER. Analytics SHOULD display
        assertTrue(globalController.displayGoogleAnalytics(getAuthenticationToken(erwin)));

        // Anonymous user will generate no Authentication and so Analytics SHOULD display
        assertTrue(globalController.displayGoogleAnalytics(null));

    }

    private Authentication getAuthenticationToken(CurrentUser currentUser) {
        return new UsernamePasswordAuthenticationToken(currentUser,
                currentUser.getPassword(), currentUser.getAuthorities());
    }

    @Test
    public void keithCanPost() throws Exception {
        assertTrue(keith.isPostUser());
    }

    @Test
    @WithAnonymousUser
    public void googleAnalyticsAnonymousUserTest() throws Exception {
        RequestBuilder request = get("/").with(csrf());
        MvcResult result = mockMvc.perform(request)
                .andReturn();

        assertTrue(result
                .getResponse()
                .getContentAsString()
                .contains(TRACKING_ID));
    }

    @Test
    @WithAdminUser
    public void googleAnalyticsAdminTest() throws Exception {
        assertEquals(siteOptions.getAddGoogleAnalytics(), true);
        RequestBuilder request = get("/").with(csrf());
        MvcResult result = mockMvc.perform(request)
                .andReturn();

        assertFalse(result
                .getResponse()
                .getContentAsString()
                .contains(TRACKING_ID));
    }

    @Test
    public void googleAnalyticsPostUserTest() throws Exception {
        // Keith can post as member of ROLE_POSTS
        RequestBuilder request = get("/").with(user(keith)).with(csrf());
        MvcResult result = mockMvc.perform(request)
                .andReturn();

        assertFalse(result
                .getResponse()
                .getContentAsString()
                .contains(TRACKING_ID));
    }
}