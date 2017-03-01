package com.nixmash.blog.jpa.components;

import com.nixmash.blog.jpa.common.ApplicationSettings;
import com.nixmash.blog.jpa.common.ISiteOption;
import com.nixmash.blog.jpa.common.SiteOptions;
import com.nixmash.blog.jpa.dto.AlphabetDTO;
import com.nixmash.blog.jpa.dto.CategoryDTO;
import com.nixmash.blog.jpa.dto.PostDTO;
import com.nixmash.blog.jpa.dto.SiteOptionDTO;
import com.nixmash.blog.jpa.enums.BatchJobName;
import com.nixmash.blog.jpa.enums.PostDisplayType;
import com.nixmash.blog.jpa.enums.PostType;
import com.nixmash.blog.jpa.exceptions.DuplicatePostNameException;
import com.nixmash.blog.jpa.exceptions.SiteOptionNotFoundException;
import com.nixmash.blog.jpa.model.BatchJob;
import com.nixmash.blog.jpa.model.GitHubStats;
import com.nixmash.blog.jpa.model.Post;
import com.nixmash.blog.jpa.service.PostService;
import com.nixmash.blog.jpa.service.SiteService;
import com.nixmash.blog.jpa.service.StatService;
import com.nixmash.blog.jpa.service.UserService;
import com.nixmash.blog.jpa.utils.PostUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

import static com.nixmash.blog.jpa.utils.SharedUtils.timeMark;
import static com.nixmash.blog.jpa.utils.SharedUtils.totalTime;

@Component
public class JpaUI {

    private static final Logger logger = LoggerFactory.getLogger(JpaUI.class);
    

    // region  Beans

    private final PostService postService;
    private final UserService userService;
    private final SiteService siteService;
    private final ApplicationSettings applicationSettings;
    final DefaultListableBeanFactory beanfactory;
    private final SiteOptions siteOptions;
    private final StatService statService;
    private final Environment environment;

    // endregion

    @Autowired
    public JpaUI(PostService postService, SiteOptions siteOptions, UserService userService, ApplicationSettings applicationSettings, DefaultListableBeanFactory beanfactory, SiteService siteService, StatService statService, Environment environment) {
        this.postService = postService;
        this.siteOptions = siteOptions;
        this.userService = userService;
        this.applicationSettings = applicationSettings;
        this.beanfactory = beanfactory;
        this.siteService = siteService;
        this.statService = statService;
        this.environment = environment;
    }

    public void init() {
        String activeProfile = environment.getActiveProfiles()[0];
        logger.info(String.format("Current JPA Active Profile: %s", activeProfile));

        displayCategoryCounts();
    }

    // region Categories

    private void displayCategoryCounts() {
        List<CategoryDTO> categoryDTOS = postService.getCategoryCounts();
        for (CategoryDTO categoryDTO : categoryDTOS) {
            System.out.println(MessageFormat.format("{0} | {1} | {2}",
                    categoryDTO.getCategoryId(), categoryDTO.getCategoryValue(), categoryDTO.getCategoryCount()));
        }
    }
    // endregion

    // region BatchJob Reports and GitHub Stats

    private void getGithubStats() {
        GitHubStats gitHubStats = statService.getCurrentGitHubStats();
        if (gitHubStats != null)
            System.out.println("GitHubStats Lives");
        else
            System.out.println("GitHubStats is null");
    }

    private void getBatchJobs() {
        List<BatchJob> batchJobs = statService.getBatchJobsByJob(BatchJobName.GITHUBSTATS);
        for (BatchJob batchJob :
                batchJobs) {
            System.out.println(batchJob);
        }
    }

    // endregion

    // region Posts

    private void displayPosts() {
        List<Post> posts = postService.getAllPosts();
        for (Post post : posts) {
            System.out.println(post.getPostId() + " : " + post.getPostTitle());
        }
    }

    // endregion

    // region cache play

    public void allPublishedPostsCache() {
        List<Post> posts;
        long start;
        long end;

        System.out.println();
        start = timeMark();
        postService.getPublishedPosts(0, 25);
        end = timeMark();
        System.out.println("Retrieval time getPublishedPosts(0, 25): " + totalTime(start, end));

        start = timeMark();
        postService.getPublishedPosts(0, 25);
        end = timeMark();
        System.out.println("Repeat retrieval time getPublishedPosts(0, 25): " + totalTime(start, end));

        System.out.println();
        start = timeMark();
        postService.getPublishedPosts(1, 25);
        end = timeMark();
        System.out.println("Retrieval time getPublishedPosts(1, 25): " + totalTime(start, end));

        start = timeMark();
        postService.getPublishedPosts(1, 25);
        end = timeMark();
        System.out.println("Repeat retrieval time getPublishedPosts(1, 25): " + totalTime(start, end));

    }

    // endregion

    private void generateAlphabet() {

        List<AlphabetDTO> alphaLinks = postService.getAlphaLInks();
        for (AlphabetDTO alphaLink : alphaLinks) {
            System.out.println(alphaLink.getAlphaCharacter() + " " + alphaLink.getActive());
        }
    }

    private void displayRandomUserIdString() {
        System.out.println(RandomStringUtils.randomAlphanumeric(16));
    }

    private void addPostDemo() throws DuplicatePostNameException {
        String title = "Best way to create SEO friendly URI string";
        PostDTO postDTO = PostDTO.getBuilder(
                1L,
                title,
                PostUtils.createSlug(title),
                "http://nixmash.com/java/variations-on-json-key-value-pairs-in-spring-mvc/",
                "This is the post content",
                PostType.LINK,
                PostDisplayType.LINK_FEATURE
        ).build();
        postService.add(postDTO);
    }

    private void siteOptionsDemo() {
        System.out.println("Initialized SiteOptions Bean Property: " +
                siteOptions.getGoogleAnalyticsTrackingId());

        Boolean reset = true;
        String siteName = reset ? "My Site" : "My Updated Site Name";
        String integerProperty = reset ? "1" : "8";
        String userRegistration = "EMAIL_VERIFICATION";

        try {
            siteService.update(new SiteOptionDTO(ISiteOption.SITE_NAME, siteName));
            siteService.update(new SiteOptionDTO(ISiteOption.INTEGER_PROPERTY, integerProperty));
            siteService.update(new SiteOptionDTO(ISiteOption.USER_REGISTRATION, userRegistration));
        } catch (SiteOptionNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("New SiteOptions values: " + siteOptions.getSiteName() + " -- " + siteOptions.getIntegerProperty());
        System.out.println("GoogleAnalyticsId: " + siteOptions.getGoogleAnalyticsTrackingId());
        System.out.println("UserRegistration: " + siteOptions.getUserRegistration());
    }

}
