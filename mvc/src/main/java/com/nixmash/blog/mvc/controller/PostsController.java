package com.nixmash.blog.mvc.controller;

import com.nixmash.blog.jpa.common.ApplicationSettings;
import com.nixmash.blog.jpa.dto.PostQueryDTO;
import com.nixmash.blog.jpa.enums.PostType;
import com.nixmash.blog.jpa.exceptions.TagNotFoundException;
import com.nixmash.blog.jpa.model.Tag;
import com.nixmash.blog.jpa.service.PostService;
import com.nixmash.blog.jsoup.service.JsoupService;
import com.nixmash.blog.mail.service.FmService;
import com.nixmash.blog.mvc.components.WebUI;
import com.nixmash.blog.solr.model.PostDoc;
import com.nixmash.blog.solr.service.PostDocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by daveburke on 5/27/16.
 */
@SuppressWarnings("Duplicates")
@Controller
@RequestMapping(value = "/posts")
public class PostsController {

    private static final Logger logger = LoggerFactory.getLogger(PostsController.class);

    // region constants

    protected static final String POSTS_LIST_VIEW = "posts/list";
    public static final String POSTS_TITLES_VIEW = "posts/titles";
    public static final String POSTS_TAGCLOUD_VIEW = "posts/tagcloud";
    private static final String POSTS_TAGS_VIEW = "posts/tags";
    private static final String POSTS_TAGTITLES_VIEW = "posts/tagtitles";
    public static final String POSTS_LIKES_VIEW = "posts/likes";
    public static final String POSTS_AZ_VIEW = "posts/az";
    public static final String POSTS_SEARCH_VIEW = "posts/search";
    public static final String POSTS_QUICKSEARCH_VIEW = "posts/quicksearch";
    public static final String POSTS_LINKS_VIEW = "posts/links";

    public static int POST_PAGING_SIZE;
    public static int TITLE_PAGING_SIZE;

    public static final String SESSION_QUICKSEARCH_QUERY = "quicksearch";
    public static final String SESSION_POSTQUERYDTO = "postquerydto";

    // endregion

    // region beans

    private final WebUI webUI;
    private final JsoupService jsoupService;
    private final PostService postService;
    private final ApplicationSettings applicationSettings;
    private final FmService fmService;
    private final PostDocService postDocService;

    // endregion

    // region constructor

    @Autowired
    public PostsController(WebUI webUI, JsoupService jsoupService, PostService postService, ApplicationSettings applicationSettings, FmService fmService, PostDocService postDocService) {
        this.webUI = webUI;
        this.jsoupService = jsoupService;
        this.postService = postService;
        this.applicationSettings = applicationSettings;
        this.fmService = fmService;
        this.postDocService = postDocService;

        POST_PAGING_SIZE = applicationSettings.getPostStreamPageCount();
        TITLE_PAGING_SIZE = applicationSettings.getPostTitleStreamPageCount();
    }

    // endregion

    // region /posts get

    @RequestMapping(value = "", method = GET)
    public String home(Model model) {
        boolean showMore = postService.getAllPublishedPosts().size() > TITLE_PAGING_SIZE;
        model.addAttribute("showmore", showMore);
        return POSTS_LIST_VIEW;
    }

    @RequestMapping(value = "/search", method = GET)
    public String searchPage(Model model, HttpServletRequest request) {
        model.addAttribute("postQueryDTO", new PostQueryDTO());
        model.addAttribute("isSearchResult", false);
        WebUtils.setSessionAttribute(request, SESSION_POSTQUERYDTO, null);
        return POSTS_SEARCH_VIEW;
    }

    @RequestMapping(value = "/search",  params = {"query"}, method = GET)
    public String searchPageResults(@Valid PostQueryDTO postQueryDTO,
                                    BindingResult result, Model model, HttpServletRequest request) {
        model.addAttribute("postQuery", postQueryDTO);
        if (result.hasErrors()) {
            return POSTS_SEARCH_VIEW;
        } else {
            WebUtils.setSessionAttribute(request, SESSION_POSTQUERYDTO, postQueryDTO);
            model.addAttribute("isSearchResult", true);
            return POSTS_SEARCH_VIEW;
        }
    }

    @RequestMapping(value = "", params = {"search"}, method = GET)
    public String quicksearch(Model model, String search, HttpServletRequest request) {
        List<PostDoc> postDocs = postDocService.doQuickSearch(search);

        boolean showMore = postDocs.size() > POST_PAGING_SIZE;
        boolean hasQuickSearchResults = postDocs.size() > 0;

        WebUtils.setSessionAttribute(request, SESSION_QUICKSEARCH_QUERY, search);
        model.addAttribute("showmore", showMore);
        model.addAttribute("query", search);
        model.addAttribute("hasResults", hasQuickSearchResults);
        return POSTS_QUICKSEARCH_VIEW;
    }


    @RequestMapping(value = "/feed", produces = "application/*")
    public String feed() {
        return "rssPostFeedView";
    }

    @RequestMapping(value = "/titles", method = GET)
    public String titles(Model model) {
        boolean showMore = postService.getAllPublishedPosts().size() > TITLE_PAGING_SIZE;
        model.addAttribute("showmore", showMore);
        return POSTS_TITLES_VIEW;
    }

    @RequestMapping(value = "/links", method = GET)
    public String justlinks(Model model) {
        boolean showMore = postService.getAllPublishedPostsByPostType(PostType.LINK).size() > POST_PAGING_SIZE;
        model.addAttribute("showmore", showMore);
        return POSTS_LINKS_VIEW;
    }

    @RequestMapping(value = "/tagcloud", method = GET)
    public String postsTagcloudPage(Model model) {
        return POSTS_TAGCLOUD_VIEW;
    }

    @RequestMapping(value = "/az", method = GET)
    public String postsAtoZ(Model model) {
        return POSTS_AZ_VIEW;
    }

    @RequestMapping(value = "/tag/{tagValue}", method = GET)
    public String tags(@PathVariable("tagValue") String tagValue, Model model)
            throws TagNotFoundException, UnsupportedEncodingException {
        Tag tag = postService.getTag(URLDecoder.decode(tagValue, "UTF-8"));
        boolean showMore = postService.getPublishedPostsByTagId(tag.getTagId()).size() > POST_PAGING_SIZE;
        model.addAttribute("tag", tag);
        model.addAttribute("showmore", showMore);
        return POSTS_TAGS_VIEW;
    }

    @RequestMapping(value = "/likes/{userId}", method = GET)
    public String userLikes(@PathVariable("userId") long userId, Model model) {
        boolean showMore = false;
        if (postService.getPostsByUserLikes(userId) != null)
            showMore = postService.getPostsByUserLikes(userId).size() > POST_PAGING_SIZE;
        model.addAttribute("showmore", showMore);
        return POSTS_LIKES_VIEW;
    }

    @RequestMapping(value = "/titles/tag/{tagValue}", method = GET)
    public String tagTitles(@PathVariable("tagValue") String tagValue, Model model)
            throws TagNotFoundException, UnsupportedEncodingException {
        Tag tag = postService.getTag(URLDecoder.decode(tagValue, "UTF-8"));
        boolean showMore = postService.getPublishedPostsByTagId(tag.getTagId()).size() > TITLE_PAGING_SIZE;
        model.addAttribute("tag", tag);
        model.addAttribute("showmore", showMore);
        return POSTS_TAGTITLES_VIEW;
    }

    // endregion

}