package com.nixmash.blog.mvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by daveburke on 3/26/17.
 */
@Controller
public class NixMashController {

    // region Constants

    private static final Logger logger = LoggerFactory.getLogger(NixMashController.class);

    private static final String SERVICES_VIEW = "business/services";

    // endregion

    // region Pages

    @GetMapping(value = "/services")
    public String services() {
        return SERVICES_VIEW;
    }

    // endregion

}
