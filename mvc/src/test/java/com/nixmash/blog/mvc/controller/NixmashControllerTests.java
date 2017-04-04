package com.nixmash.blog.mvc.controller;

import com.nixmash.blog.jpa.utils.SharedUtils;
import com.nixmash.blog.mvc.AbstractContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by daveburke on 3/26/17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = {"nixmash.mode.enabled = true"})
public class NixmashControllerTests extends AbstractContext {


    // region Beans

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private Environment environment;

    private MockMvc mockMvc;

    // endregion

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void loadServicesPage() throws Exception {
        environment.getProperty("nixmash.mode.enabled");

        if (SharedUtils.isNixMashPC()) {
            mockMvc.perform(get("/services"))
                    .andExpect(status().isOk());
        }
    }

}
