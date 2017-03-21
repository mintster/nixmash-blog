package com.nixmash.blog.mail.service;

import com.nixmash.blog.jpa.model.Post;
import com.nixmash.blog.jpa.model.PostMeta;
import com.nixmash.blog.jpa.model.User;

public interface FmService {

    String displayTestTemplate(User user);

    String getNoLinksMessage();

    String getNoResultsMessage(String search);

    String getNoLikesMessage();

    String createRssPostContent(Post post);

    String createPostHtml(Post post, String templateName);

    String createPostHtml(Post post);

    String createPostAtoZs();

    String getNoMoreLikeThisMessage();

    String getRobotsTxt();

    String getFileUploadingScript();

    String getFileUploadedScript();

    String getTwitterTemplate(PostMeta postMeta);
}
