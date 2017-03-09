package com.nixmash.blog.jsoup.dto;

import com.nixmash.blog.jpa.enums.PostDisplayType;
import com.nixmash.blog.jsoup.annotations.DocText;
import com.nixmash.blog.jsoup.annotations.ImageSelector;
import com.nixmash.blog.jsoup.base.JsoupImage;

import java.io.Serializable;
import java.util.List;

/**
 * Created by daveburke on 5/19/16.
 */
@SuppressWarnings("WeakerAccess")


public class JsoupPostDTO implements Serializable {

    private static final long serialVersionUID = -2690151588053584076L;

    @DocText
    public String bodyText;

    @ImageSelector
    public List<JsoupImage> imagesInContent;
    public boolean hasImages() {
        { return  (!this.imagesInContent.isEmpty());}
    }

    public String twitterImagePath;
    public String twitterDescription;
    public PostDisplayType postDisplayType;

    // region getters setters


    public PostDisplayType getPostDisplayType() {
        return postDisplayType;
    }
    public void setPostDisplayType(PostDisplayType postDisplayType) {
        this.postDisplayType = postDisplayType;
    }

    public List<JsoupImage> getImagesInContent() {
        return imagesInContent;
    }
    public void setImagesInContent(List<JsoupImage> imagesInContent) {
        this.imagesInContent = imagesInContent;
    }

    public String getBodyText() {
        return bodyText;
    }
    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public String getTwitterImagePath() {
        return twitterImagePath;
    }
    public void setTwitterImagePath(String twitterImagePath) {
        this.twitterImagePath = twitterImagePath;
    }

    public String getTwitterDescription() {
        return twitterDescription;
    }
    public void setTwitterDescription(String twitterDescription) {
        this.twitterDescription = twitterDescription;
    }

// endregion



}
