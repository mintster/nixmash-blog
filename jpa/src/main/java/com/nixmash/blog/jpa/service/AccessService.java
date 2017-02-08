package com.nixmash.blog.jpa.service;

import com.nixmash.blog.jpa.dto.AccessDTO;

/**
 * Created by daveburke on 12/10/16.
 */
public interface AccessService {

    boolean isEmailApproved(String email);

    AccessDTO createAccessDTO(String email);
}
