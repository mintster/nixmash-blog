package com.nixmash.blog.solr.service;

import com.nixmash.blog.jpa.common.ApplicationSettings;
import com.nixmash.blog.jpa.dto.PostQueryDTO;
import com.nixmash.blog.jpa.exceptions.PostNotFoundException;
import com.nixmash.blog.jpa.model.Post;
import com.nixmash.blog.jpa.service.PostService;
import com.nixmash.blog.solr.model.PostDoc;
import com.nixmash.blog.solr.repository.custom.CustomPostDocRepository;
import com.nixmash.blog.solr.utils.SolrUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.UncategorizedSolrException;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class PostDocServiceImpl implements PostDocService {

    private static final Logger logger = LoggerFactory.getLogger(PostDocServiceImpl.class);

    @Resource
    CustomPostDocRepository customPostDocRepository;

    private final SolrOperations solrOperations;
    private final PostService postService;
    private final ApplicationSettings applicationSettings;


    @Autowired
    public PostDocServiceImpl(SolrOperations solrOperations, PostService postService, ApplicationSettings applicationSettings) {
        this.solrOperations = solrOperations;
        this.postService = postService;
        this.applicationSettings = applicationSettings;
    }


    // region PostDoc to Post Conversion

    @Transactional(readOnly = true)
    @Override
    public List<Post> getPostsFromPostDocs(List<PostDoc> postDocs) {
        List<Post> posts = new ArrayList<>();
        for (PostDoc postDoc :
                postDocs) {
            try {
                posts.add(postService.getPostById(Long.parseLong(postDoc.getPostId())));
            } catch (PostNotFoundException e) {
                logger.info("Could not convert PostDoc {} to Post with title \"{}\"", postDoc.getPostId(), postDoc.getPostTitle());
            }
        }
        return posts;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Post> getMoreLikeThisPostsFromPostDocs(List<PostDoc> postDocs) {
        List<Post> posts = new ArrayList<>();
        String result = StringUtils.EMPTY;

        for (int i = 0; i < applicationSettings.getMoreLikeThisNum(); i++) {
            try {
                PostDoc postDoc = postDocs.get(i);
                posts.add(postService.getPostById(Long.parseLong(postDoc.getPostId())));
            } catch (PostNotFoundException | IndexOutOfBoundsException e) {
                if (e.getClass().equals(PostNotFoundException.class))
                    logger.info("MoreLikeThis PostDoc {} to Post with title \"{}\" NOT FOUND", postDocs.get(i).getPostId(), postDocs.get(i).getPostTitle());
                else {
                    logger.info("EXCEPTION: AppSetting.MoreLikeThisNum > post count");
                    return null;
                }
            }
        }
        return posts;
    }
    // endregion

    @Override
    public List<PostDoc> getPostsWithUserQuery(String userQuery) {
        logger.debug("SimpleQuery from user search string -  findProductsBySimpleQuery()");
        return customPostDocRepository.findPostsBySimpleQuery(userQuery);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PostDoc> getMoreLikeThis(Long postId) {
        List<PostDoc> postDocList = null;
        try {
            postDocList = customPostDocRepository.findMoreLikeThis(postId);
        } catch (UncategorizedSolrException e) {
            logger.info("MoreLikeThis posts not retrieved for postId " + String.valueOf(postId));
            postDocList = null;
        }
        return postDocList;
    }

    @Transactional
    @Override
    public void addToIndex(Post post) {
        logger.debug("Saving a Post Document with information: {}", post);
        PostDoc document = SolrUtils.createPostDoc(post);
        customPostDocRepository.save(document);
        commit();
    }

    @Transactional
    @Override
    public void reindexPosts(List<Post> posts) {
        Query query = new SimpleQuery(new SimpleStringCriteria("doctype:post"));
        solrOperations.delete(query);
        solrOperations.commit();
        addAllToIndex(posts);
    }

    @Transactional
    @Override
    public void addAllToIndex(List<Post> posts) {
        logger.debug("Saving all Post Documents to Index");
        List<PostDoc> postDocs = new ArrayList<>();
        for (Post post : posts
                ) {
            postDocs.add(SolrUtils.createPostDoc(post));
        }
        customPostDocRepository.save(postDocs);
        commit();
    }

    @Transactional
    @Override
    public void removeFromIndex(PostDoc postDoc) {
        customPostDocRepository.delete(postDoc);
        commit();
    }

    @Transactional
    @Override
    public void removeFromIndex(List<PostDoc> postDocs) {
        customPostDocRepository.delete(postDocs);
        commit();
    }

    @Transactional
    @Override
    public void removeFromIndex(String query) {
        solrOperations.delete(new SimpleQuery(query));
        commit();
    }

    private void commit() {
        solrOperations.getSolrClient();
        solrOperations.commit();
    }

    @Override
    public void updatePostDocument(Post post) {
        customPostDocRepository.update(post);
    }

    @Override
    public PostDoc getPostDocByPostId(long postId) {
        return customPostDocRepository.findPostDocByPostId(postId);
    }

    @Override
    public List<PostDoc> getAllPostDocuments() {
        return customPostDocRepository.findAllPostDocuments();
    }

    @Override
    public List<PostDoc> doQuickSearch(String searchTerm) {
        return customPostDocRepository.quickSearch(searchTerm);
    }

    @Override
    public List<PostDoc> doFullSearch(PostQueryDTO postQueryDTO) {
        return customPostDocRepository.fullSearch(postQueryDTO);
    }

    @Override
    public Page<PostDoc> doPagedFullSearch(PostQueryDTO postQueryDTO, int pageNumber, int pageSize) {
        PageRequest pageRequest =
                new PageRequest(pageNumber, pageSize, sortByPostDateDesc());
        return customPostDocRepository.pagedFullSearch(postQueryDTO, pageRequest);
    }

    @Override
    public Page<PostDoc> doPagedQuickSearch(String searchTerms, int pageNumber, int pageSize) {
        PageRequest pageRequest =
                new PageRequest(pageNumber, pageSize, sortByPostDateDesc());
        return customPostDocRepository.pagedQuickSearch(searchTerms, pageRequest);
    }

    public Sort sortByPostDateDesc() {
        return new Sort(Sort.Direction.DESC, PostDoc.POST_DATE);
    }
}
