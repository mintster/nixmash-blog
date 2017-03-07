package com.nixmash.blog.jpa.model;

import com.nixmash.blog.jpa.enums.TwitterCardType;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by daveburke on 3/7/17.
 */
@Entity
@Table(name = "post_meta")
public class PostMeta implements Serializable {

    private static final long serialVersionUID = 7743331633690910405L;
    private Long postId;
    private String twitterCreator;
    private String twitterImage;
    private String twitterDescription;
    private TwitterCardType twitterCardType;

    @Id
    @Column(name = "post_id")
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    @Basic
    @Column(name = "twitter_card")
    @Enumerated(EnumType.STRING)
    public TwitterCardType getTwitterCardType() {
        return twitterCardType;
    }

    public void setTwitterCardType(TwitterCardType twitterCardType) {
        this.twitterCardType = twitterCardType;
    }

    @Basic
    @Column(name = "twitter_creator")
    public String getTwitterCreator() {
        return twitterCreator;
    }

    public void setTwitterCreator(String twitterCreator) {
        this.twitterCreator = twitterCreator;
    }

    @Basic
    @Column(name = "twitter_image")
    public String getTwitterImage() {
        return twitterImage;
    }

    public void setTwitterImage(String twitterImage) {
        this.twitterImage = twitterImage;
    }

    @Basic
    @Column(name = "twitter_description")
    public String getTwitterDescription() {
        return twitterDescription;
    }

    public void setTwitterDescription(String twitterDescription) {
        this.twitterDescription = twitterDescription;
    }
}
