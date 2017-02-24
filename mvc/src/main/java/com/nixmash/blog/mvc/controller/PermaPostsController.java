package com.nixmash.blog.mvc.controller;

import com.nixmash.blog.jpa.common.ApplicationSettings;
import com.nixmash.blog.jpa.exceptions.PostCategoryNotSupportedException;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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

    // region Handling NixMash Post {category}/{postname} Permalink  to /post/{postname}

    @ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
    @GetMapping(value = "/{category:java|linux|postgresql|mysql|wordpress|android|codejohnny}/{postName}")
    public String categoryPost() {
        return "redirect:/post/{postName}";
    }

    @ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
    @GetMapping(value = "/{category:ruby-on-rails|nixmashup|php|best-of-everyman-links|drupal}/{postName}")
    public String oldCategoryPost(HttpServletRequest request,
                                  @PathVariable("category") String category,
                                  @PathVariable("postName") String postName) {
        request.setAttribute("category", category);
        request.setAttribute("postName", postName);
        throw new PostCategoryNotSupportedException();
    }

    // endregion

    @GetMapping(value = "/post/{postName}")
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
