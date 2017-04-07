package com.nixmash.blog.jpa.service;

import com.nixmash.blog.jpa.dto.SiteOptionDTO;
import com.nixmash.blog.jpa.exceptions.SiteOptionNotFoundException;
import com.nixmash.blog.jpa.model.SiteImage;
import com.nixmash.blog.jpa.model.SiteOption;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by daveburke on 5/7/16.
 */
public interface SiteService {

    @Transactional
    SiteImage getHomeBanner();

    SiteOption update(SiteOptionDTO siteOptionDTO) throws SiteOptionNotFoundException;

    @Transactional(readOnly = true)
    SiteOption findOptionByName(String name) throws SiteOptionNotFoundException;

}
