package com.nixmash.blog.jpa.service;

import com.nixmash.blog.jpa.model.BatchJob;
import com.nixmash.blog.jpa.model.GitHubStats;

import java.util.List;

/**
 * Created by daveburke on 12/2/16.
 */
public interface StatService {

    GitHubStats getCurrentGitHubStats();

    List<BatchJob> getBatchJobsByJob(String jobName);
}
