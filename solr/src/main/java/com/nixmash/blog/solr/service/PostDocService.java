package com.nixmash.blog.solr.service;

import com.nixmash.blog.jpa.dto.PostQueryDTO;
import com.nixmash.blog.jpa.model.Post;
import com.nixmash.blog.solr.model.PostDoc;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by daveburke on 9/29/16.
 */
public interface PostDocService {

    List<PostDoc> getPostsWithUserQuery(String userQuery);

    void addToIndex(Post post);

    @Transactional
    void reindexPosts(List<Post> posts);

    void addAllToIndex(List<Post> posts);

    void removeFromIndex(PostDoc postDoc);

    void removeFromIndex(List<PostDoc> postDocs);

    void removeFromIndex(String query);

    void updatePostDocument(Post post);

    PostDoc getPostDocByPostId(long postId);

    List<PostDoc> getAllPostDocuments();

    List<PostDoc> doQuickSearch(String searchTerm);

    List<PostDoc> doFullSearch(PostQueryDTO postQueryDTO);

    Page<PostDoc> doPagedFullSearch(PostQueryDTO postQueryDTO, int pageNumber, int pageSize);

    Page<PostDoc> doPagedQuickSearch(String searchTerms, int pageNumber, int pageSize);
}
