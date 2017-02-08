package com.nixmash.blog.jpa.repository;

import com.nixmash.blog.jpa.model.Authority;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {

    Authority findByAuthority(String authority);
    Collection<Authority> findAll() throws DataAccessException;

}
