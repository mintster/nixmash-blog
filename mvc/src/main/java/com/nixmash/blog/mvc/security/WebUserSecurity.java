package com.nixmash.blog.mvc.security;

import com.nixmash.blog.jpa.model.CurrentUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static org.springframework.security.config.Elements.ANONYMOUS;

/**
 * Created by daveburke on 2/22/17.
 */
@Component
public class WebUserSecurity {

    public boolean canAccessAdmin(Authentication authentication) {
        if (authentication.getName().equals(ANONYMOUS)) {
            return false;
        } else {
            return ((CurrentUser) authentication.getPrincipal()).canAccessAdmin();
        }
    }

}
