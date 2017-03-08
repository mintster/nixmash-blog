package com.nixmash.blog.jpa.repository;

import com.nixmash.blog.jpa.model.PostMeta;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by daveburke on 12/19/16.
 */
public interface PostMetaRepository extends CrudRepository<PostMeta, Long>{
    PostMeta findByPostId(Long postId);
    List<PostMeta> findAll();
}

