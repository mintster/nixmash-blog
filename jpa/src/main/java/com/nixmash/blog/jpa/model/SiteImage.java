package com.nixmash.blog.jpa.model;

import javax.persistence.*;

/**
 * Created by daveburke on 4/7/17.
 */
@Entity
@Table(name = "site_images")
public class SiteImage {
    private Long siteImageId;
    private String imageFilename;
    private String imageDescription;
    private String imageAuthor;
    private String sourceUrl;
    private Boolean commonLicense;
    private Boolean bannerImage;
    private Boolean isActive;

    private String imageMessage;

    @Id
    @Column(name = "site_image_id")
    public Long getSiteImageId() {
        return siteImageId;
    }

    public void setSiteImageId(Long siteImageId) {
        this.siteImageId = siteImageId;
    }

    @Basic
    @Column(name = "image_filename")
    public String getImageFilename() {
        return imageFilename;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }

    @Basic
    @Column(name = "image_description")
    public String getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }

    @Basic
    @Column(name = "image_author")
    public String getImageAuthor() {
        return imageAuthor;
    }

    public void setImageAuthor(String imageAuthor) {
        this.imageAuthor = imageAuthor;
    }

    @Basic
    @Column(name = "source_url")
    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    @Basic
    @Column(name = "common_license")
    public Boolean getCommonLicense() {
        return commonLicense;
    }

    public void setCommonLicense(Boolean commonLicense) {
        this.commonLicense = commonLicense;
    }

    @Basic
    @Column(name = "banner_image")
    public Boolean getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(Boolean bannerImage) {
        this.bannerImage = bannerImage;
    }

    @Basic
    @Column(name = "is_active", nullable = false)
    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Transient
    public Boolean isOwned() {
        return getImageAuthor().equals("SITE");
    }

    @Transient
    public String getImageMessage() {
        return imageMessage;
    }

    public void setImageMessage(String imageMessage) {
        this.imageMessage = imageMessage;
    }
}
