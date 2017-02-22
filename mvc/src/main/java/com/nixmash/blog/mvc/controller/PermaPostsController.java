package com.nixmash.blog.mvc.controller;

import com.nixmash.blog.jpa.common.ApplicationSettings;
import com.nixmash.blog.jpa.exceptions.PostNotFoundException;
import com.nixmash.blog.jpa.model.CurrentUser;
import com.nixmash.blog.jpa.model.Post;
import com.nixmash.blog.jpa.service.PostService;
import com.nixmash.blog.jpa.utils.PostUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by daveburke on 2/22/17.
 */
@Controller
public class PermaPostsController {

    private static final Logger logger = LoggerFactory.getLogger(PermaPostsController.class);

    public static final String POSTS_PERMALINK_VIEW = "posts/post";

    private final PostService postService;
    private final ApplicationSettings applicationSettings;

    public PermaPostsController(PostService postService, ApplicationSettings applicationSettings) {
        this.postService = postService;
        this.applicationSettings = applicationSettings;
    }

    @ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
    @RequestMapping(value = "/{category}/{postName}", method = GET)
    public String post(@PathVariable("category") String category, @PathVariable("postName") String postName,
                       Model model, CurrentUser currentUser) throws PostNotFoundException {
        logger.debug("in category/postname -------------------------------------------------------------- */");
        return post(postName, model, currentUser);
    }

    @RequestMapping(value = "/post/{postName}", method = GET)
    public String post(@PathVariable("postName") String postName, Model model, CurrentUser currentUser)
            throws PostNotFoundException {

        Post post = postService.getPost(postName);
        Date postCreated = Date.from(post.getPostDate().toInstant());
        post.setIsOwner(PostUtils.isPostOwner(currentUser, post.getUserId()));
        post.setPostContent(PostUtils.formatPostContent(post));
        model.addAttribute("post", post);
        model.addAttribute("postCreated", postCreated);
        model.addAttribute("shareSiteName",
                StringUtils.deleteWhitespace(applicationSettings.getSiteName()));
        model.addAttribute("shareUrl",
                String.format("%s/post/%s", applicationSettings.getBaseUrl(), post.getPostName()));
        return POSTS_PERMALINK_VIEW;
    }



}
