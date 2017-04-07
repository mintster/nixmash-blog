package com.nixmash.blog.jpa.repository;

import com.nixmash.blog.jpa.model.SiteImage;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface SiteImageRepository extends CrudRepository<SiteImage, Long> {

    SiteImage findBySiteImageId(Long id);
    Collection<SiteImage> findAll();

}
