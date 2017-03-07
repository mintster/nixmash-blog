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

    private String twitterTitle;
    private String twitterUrl;
    private String twitterSite;

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

    @Transient
    public String getTwitterTitle() {
        return twitterTitle;
    }

    public void setTwitterTitle(String twitterTitle) {
        this.twitterTitle = twitterTitle;
    }

    @Transient
    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    @Transient
    public String getTwitterSite() {
        return twitterSite;
    }

    public void setTwitterSite(String twitterSite) {
        this.twitterSite = twitterSite;
    }

    public static Builder getBuilder(TwitterCardType twitterCardType, String twitterTitle, String twitterSite, String twitterCreator) {
        return new PostMeta.Builder(twitterCardType, twitterTitle, twitterSite, twitterCreator);
    }

    public static Builder getUpdated(TwitterCardType twitterCardType, String twitterImage, String twitterDescription) {
        return new PostMeta.Builder(twitterCardType, twitterImage, twitterDescription);
    }

    public static class Builder {

        private PostMeta built;

        public Builder(TwitterCardType twitterCardType, String twitterTitle, String twitterSite, String twitterCreator) {
            built = new PostMeta();
            built.twitterCardType = twitterCardType;
            built.twitterTitle = twitterTitle;
            built.twitterSite = twitterSite;
            built.twitterCreator = twitterCreator;
        }

        public Builder(TwitterCardType twitterCardType, String twitterImage, String twitterDescription) {
            built = new PostMeta();
            built.twitterCardType = twitterCardType;
            built.twitterImage = twitterImage;
            built.twitterDescription = twitterDescription;
        }

        public Builder twitterCreator(String creator)
        {
            built.twitterCreator= creator;
            return this;
        }

        public Builder twitterDescription(String description)
        {
            built.twitterDescription = description;
            return this;
        }


        public Builder twitterUrl(String postUrl)
        {
            built.twitterUrl = postUrl;
            return this;
        }

        public Builder twitterImage(String twitterImage) {
            built.twitterImage = twitterImage;
            return this;
        }

        public PostMeta build() {
            return built;
        }

    }
}
