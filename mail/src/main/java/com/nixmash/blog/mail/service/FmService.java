package com.nixmash.blog.mail.service;

import com.nixmash.blog.jpa.model.Post;
import com.nixmash.blog.jpa.model.User;

public interface FmService {

    String displayTestTemplate(User user);

    String getNoResultsMessage(String search);

    String getNoLikesMessage();

    String createPostHtml(Post post, String templateName);

    String createPostHtml(Post post);

    String createPostAtoZs();

    String getRobotsTxt();

    String getFileUploadingScript();

    String getFileUploadedScript();
}
