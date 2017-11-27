package com.nixmash.blog.jpa.repository;

import com.nixmash.blog.jpa.config.ApplicationConfig;
import com.nixmash.blog.jpa.enums.DataConfigProfile;
import com.nixmash.blog.jpa.model.SiteImage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;

/**
 * Created by daveburke on 5/31/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@ActiveProfiles(DataConfigProfile.H2)
public class SiteImageRepoTests {

    @Autowired
    SiteImageRepository siteImageRepository;

    @Test
    public void getCurrentImageTest() {
        // Current SiteImage Id = 2 and dayOfYear = 100
        SiteImage siteImage = siteImageRepository.findByIsCurrentTrueAndDayOfYear(1);
        Assert.assertTrue(siteImage.getIsCurrent());

        SiteImage nullImage = siteImageRepository.findByIsCurrentTrueAndDayOfYear(1000);
        Assert.assertNull(nullImage);
    }

    @Test
    public void getNonCurrentImageTest() {
        SiteImage siteImage = siteImageRepository.findBySiteImageId(1L);
        Assert.assertFalse(siteImage.getIsCurrent());
    }

    @Test
    public void setCurrentImageTest() {
        // Current SiteImage Id = 2
        // Default dayOfYear for all records = 1
        SiteImage siteImage = siteImageRepository.findBySiteImageId(2L);
        Assert.assertTrue(siteImage.getIsCurrent());

        siteImage.setIsCurrent(false);
        siteImageRepository.save(siteImage);

        SiteImage retrieved = siteImageRepository.findBySiteImageId(2L);
        Assert.assertFalse(retrieved.getIsCurrent());
    }

    @Test
    public void setCurrentImageAndDayOfYearTest() {
        // Current SiteImage Id = 2
        // Default dayOfYear for all records = 1
        SiteImage siteImage = siteImageRepository.findByIsCurrentTrueAndDayOfYear(200);
        Assert.assertNull(siteImage);

        Collection<SiteImage> all = siteImageRepository.findAll();
        all.forEach(a -> {
                    a.setDayOfYear(200);
                    if (a.getSiteImageId().equals(2L))
                        a.setIsCurrent(true);
                    else
                        a.setIsCurrent(false);
                });
        siteImageRepository.save(all);

        siteImage = siteImageRepository.findByIsCurrentTrueAndDayOfYear(200);
        Assert.assertTrue(siteImage.getIsCurrent());

    }
}
