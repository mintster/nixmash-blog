package com.nixmash.blog.mvc.controller;

import com.nixmash.blog.jpa.dto.SelectOptionDTO;
import com.nixmash.blog.jpa.model.GitHubStats;
import com.nixmash.blog.jpa.model.Post;
import com.nixmash.blog.jpa.model.SiteImage;
import com.nixmash.blog.jpa.service.PostService;
import com.nixmash.blog.jpa.service.SiteService;
import com.nixmash.blog.mail.service.FmService;
import com.nixmash.blog.mvc.components.WebUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static com.nixmash.blog.mvc.controller.GlobalController.ERROR_PAGE_MESSAGE_ATTRIBUTE;
import static com.nixmash.blog.mvc.controller.GlobalController.ERROR_PAGE_TITLE_ATTRIBUTE;
import static java.util.stream.Collectors.joining;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class GeneralController {

    // region Constants
    private static final Logger logger = LoggerFactory.getLogger(GeneralController.class);

    public static final String HOME_VIEW = "home";
    public static final String REDIRECT_HOME_VIEW = "redirect:/";
    public static final String ERROR_403_VIEW = "errors/custom";
    public static final String ERROR_404_VIEW = "errors/404";
    private static final String SERVICES_VIEW = "business/services";

    // endregion

    // region Beans

    private final FmService fmService;
    private final WebUI webUI;
    private final PostService postService;
    private final SiteService siteService;

    @Autowired
    Environment environment;

    @Autowired
    public GeneralController(FmService fmService, WebUI webUI, PostService postService, SiteService siteService) {
        this.fmService = fmService;
        this.webUI = webUI;
        this.postService = postService;
        this.siteService = siteService;
    }

    // endregion

    // region Pages

    @RequestMapping(value = "/", method = GET)
    public String home(Model model) {
        String springVersion = webUI.parameterizedMessage("home.spring.version", SpringBootVersion.getVersion());
        model.addAttribute("springVersion", springVersion);

        GitHubStats gitHubStats = webUI.getCurrentGitHubStats();
        if (gitHubStats != null) {
            model.addAttribute("showGitHubStats", true);
            model.addAttribute("gitHubStats", gitHubStats);
        }

        if (webUI.isNixMash()) {
            SiteImage siteImage = siteService.getHomeBanner();
            model.addAttribute("siteImage", siteImage);
        }

        Slice<Post> posts = postService.getPublishedPosts(0, 10);
        if (posts.getContent().size() > 0)
            model.addAttribute("posts", posts);

        return HOME_VIEW;
    }

    @RequestMapping(value = "/dev/banner", method = GET)
    public String homeBannerDisplay(Model model, @RequestParam(value = "id") long siteImageId) {
        String springVersion = webUI.parameterizedMessage("home.spring.version", SpringBootVersion.getVersion());
        model.addAttribute("springVersion", springVersion);
            SiteImage siteImage = siteService.getHomeBanner(siteImageId);
            model.addAttribute("siteImage", siteImage);

        Slice<Post> posts = postService.getPublishedPosts(0, 10);
        if (posts.getContent().size() > 0)
            model.addAttribute("posts", posts);

        return HOME_VIEW;
    }

    @RequestMapping(value = "/robots.txt", method = RequestMethod.GET)
    @ResponseBody
    public String plaintext(HttpServletResponse response) {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        return fmService.getRobotsTxt();
    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public ModelAndView accesssDenied(Principal user) {

        ModelAndView mav = new ModelAndView();
        mav.addObject(ERROR_PAGE_TITLE_ATTRIBUTE, "Not Authorized");
        mav.addObject(ERROR_PAGE_MESSAGE_ATTRIBUTE, "You are not authorized to view this page.");
        mav.setViewName(ERROR_403_VIEW);
        return mav;
    }

    @RequestMapping(value = "/404", method = RequestMethod.GET)
    public ModelAndView pageNotFound() {

        ModelAndView mav = new ModelAndView();
        mav.addObject(ERROR_PAGE_TITLE_ATTRIBUTE, "Not Found");
        mav.addObject(ERROR_PAGE_MESSAGE_ATTRIBUTE, "This page not found around town.");
        mav.setViewName(ERROR_404_VIEW);
        return mav;
    }

    @RequestMapping(value = "/json/badges/update", method = RequestMethod.POST)
    public
    @ResponseBody
    String updateBadges(@RequestBody List<String> badgeboys) {
        if (badgeboys != null) {
            String badges = badgeboys.stream().collect(joining(", "));
            logger.debug("Badge Boy Items: " + badges);
            return webUI.getMessage("js.badgeboy.result", badges);
        } else
            return "No badges selected...";
    }

    @RequestMapping(value = "/json/badges", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SelectOptionDTO> getBadges() {
        return badgeSelectOptions();
    }

    // endregion

    // region private methods

    private List<SelectOptionDTO> badgeSelectOptions() {
        List<SelectOptionDTO> selectOptionDTOs = new ArrayList<>();
        selectOptionDTOs.add(new SelectOptionDTO("Innovator", "Innovator", false));
        selectOptionDTOs.add(new SelectOptionDTO("Marathoner", "Marathoner", true));
        selectOptionDTOs.add(new SelectOptionDTO("Dude of Action", "Dude of Action", false));
        selectOptionDTOs.add(new SelectOptionDTO("Worldly", "Worldly", false));
        selectOptionDTOs.add(new SelectOptionDTO("Swell Guy", "Swell Guy", false));
        selectOptionDTOs.add(new SelectOptionDTO("Boy Scout", "Boy Scout", false));
        return selectOptionDTOs;
    }

    // endregion
}
