package com.nixmash.blog.jpa.service;

import com.nixmash.blog.jpa.model.BatchJob;
import com.nixmash.blog.jpa.model.GitHubStats;
import com.nixmash.blog.jpa.repository.BatchJobRepository;
import com.nixmash.blog.jpa.repository.GitHubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("statService")
@Transactional
public class StatServiceImpl implements StatService {

    private final GitHubRepository gitHubRepository;
    private final BatchJobRepository batchJobRepository;

    @Autowired
    public StatServiceImpl(GitHubRepository gitHubRepository, BatchJobRepository batchJobRepository) {
        this.gitHubRepository = gitHubRepository;
        this.batchJobRepository = batchJobRepository;
    }

    @Override
    public GitHubStats getCurrentGitHubStats() {
        return gitHubRepository.findTopByOrderByStatDateDesc();
    }

    @Override
    public List<BatchJob> getBatchJobsByJob(String jobName) {
            return batchJobRepository.findByJobName(jobName, sortByJobStartDateDesc());
    }

    private Sort sortByJobStartDateDesc() {
        return new Sort(Sort.Direction.DESC, "startTime");
    }

}

