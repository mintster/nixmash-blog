package com.nixmash.blog.jpa.repository;

import com.nixmash.blog.jpa.enums.PostType;
import com.nixmash.blog.jpa.model.Post;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by daveburke on 5/31/16.
 */
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {

    Post findByPostId(Long postId) throws DataAccessException;

    @Query("select distinct p from Post p left join fetch p.tags t")
    List<Post> findAllWithDetail();

    Post findByPostNameIgnoreCase(String postName) throws DataAccessException;

    @Query("select distinct p from Post p left join p.tags t where p.isPublished = true and t.tagId = ?1")
    Page<Post> findPagedPublishedByTagId(long tagId, Pageable pageable);

    @Query("select distinct p from Post p left join p.tags t where p.isPublished = true and t.tagId = ?1")
    List<Post> findAllPublishedByTagId(long tagId);

    @Query("select distinct p from Post p left join p.tags t where t.tagId = ?1")
    List<Post> findAllByTagId(long tagId);


    @Query("select distinct p from Post p left join p.category c where c.categoryId = ?1")
    List<Post> findAllByCategoryId(long categoryId);

    List<Post> findAll(Sort sort);

    List<Post> findFirst25ByOrderByPostDateDesc(Sort sort);

    List<Post> findByIsPublishedTrue(Sort sort);

    Page<Post> findByIsPublishedTrue(Pageable pageable);

    @Query(value = "SELECT " +
            "GROUP_CONCAT(distinct(upper(substr(post_title,1,1))) SEPARATOR '')" +
            " FROM posts WHERE is_published = true", nativeQuery = true)
    String getAlphaLinkString();

    @Query("select distinct p from Post p where p.displayType = 'SINGLEPHOTO_POST'")
    List<Post> findSinglePhotoPosts(Sort sort);

    @Query("select distinct p from Post p left join p.tags t where p.isPublished = true and p.postType = ?1")
    List<Post> findAllPublishedByPostType(PostType postType);

    @Query("select distinct p from Post p left join p.tags t where p.isPublished = true and p.postType = ?1")
    Page<Post> findPublishedByPostTypePaged(PostType postType, Pageable pageable);

}
