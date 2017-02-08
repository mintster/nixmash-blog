package com.nixmash.blog.jpa.repository;

import com.nixmash.blog.jpa.model.GitHubStats;
import org.springframework.data.repository.CrudRepository;

public interface GitHubRepository extends CrudRepository<GitHubStats, Long> {

    GitHubStats findOne(Long statid);

    GitHubStats findTopByOrderByStatDateDesc();

}
