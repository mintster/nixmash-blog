package com.nixmash.blog.jpa.service;

import com.nixmash.blog.jpa.SpringDataTests;
import com.nixmash.blog.jpa.common.ISiteOption;
import com.nixmash.blog.jpa.common.SiteOptions;
import com.nixmash.blog.jpa.dto.SiteOptionDTO;
import com.nixmash.blog.jpa.exceptions.SiteOptionNotFoundException;
import com.nixmash.blog.jpa.model.SiteImage;
import com.nixmash.blog.jpa.model.SiteOption;
import com.nixmash.blog.jpa.repository.SiteImageRepository;
import javassist.NotFoundException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@Transactional
public class SiteServiceTests extends SpringDataTests {

    private static final String SITE_PROPERTY_NAME = "siteName";
    private static final String INTEGER_PROPERTY_NAME = "integerProperty";
    private static final String INVALID_PROPERTY_NAME = "SchmiteName";

    @Autowired
    SiteService siteService;

    @Autowired
    SiteImageRepository siteImageRepository;

    @Autowired
    SiteOptions siteOptions;

    @Test
    public void findSiteOptionByCaseInsensitivePropertyName() throws NotFoundException, SiteOptionNotFoundException {
        SiteOption siteOption;

        siteOption = siteService.findOptionByName(SITE_PROPERTY_NAME);
        assertEquals(siteOption.getValue(), "My Site");

        siteOption = siteService.findOptionByName(SITE_PROPERTY_NAME.toLowerCase());
        assertEquals(siteOption.getValue(), "My Site");
    }

    @Test(expected = SiteOptionNotFoundException.class)
    public void invalidPropertyNameThrowsSiteOptionNotFoundException() throws Exception {
        siteService.findOptionByName(INVALID_PROPERTY_NAME);
    }

    @Test
    public void siteOptionUpdated_UpdatesSiteOptionsBean() throws SiteOptionNotFoundException {
        siteService.update(new SiteOptionDTO(SITE_PROPERTY_NAME, "Updated Site Name"));
        assert (siteOptions.getSiteName().equals("Updated Site Name"));
    }

    @Test
    public void getHomePageBannerTest() throws Exception {
        SiteImage bannerImage = siteService.getHomeBanner(1L);
        assertEquals(bannerImage.getImageFilename(), "churchstreet.jpg");
        assertTrue(getTestDayOfYear("01/01/17").equals(1));
    }

    @Test
    public void updateSiteOptionWithSiteOptionDTO() throws SiteOptionNotFoundException {

        SiteOptionDTO siteOptionDTO;
        siteOptionDTO = SiteOptionDTO.with(ISiteOption.SITE_NAME, "My Fabulous Site").build();
        siteService.update(siteOptionDTO);
        assert (siteOptions.getSiteName().equals("My Fabulous Site"));

        siteOptionDTO = SiteOptionDTO.with(ISiteOption.SITE_NAME, null).build();
        siteService.update(siteOptionDTO);
    }

    @Test
    public void completeBannerOfTheDayTest() {
        for (int i = 1; i <= 365; i++) {

            // clear all and save
            Collection<SiteImage> all = siteImageRepository.findAll();
            int finalI = i;
            all.forEach(a -> {
                a.setDayOfYear(finalI);
                a.setIsCurrent(false);
            });
            siteImageRepository.save(all);

            // filter active banners and select a random SiteImage from array
            Collection<SiteImage> siteImages = siteImageRepository.findByBannerImageTrueAndIsActiveTrue();
            int activeBannerCount = siteImages.size();
            int randomNum = ThreadLocalRandom.current().nextInt(0, activeBannerCount);
            SiteImage siteImage = (SiteImage) siteImages.toArray()[randomNum];

            // set random siteImage as current and save to database
            siteImage.setIsCurrent(true);
            siteImageRepository.save(siteImage);

            SiteImage currentSiteImage = siteImageRepository.findByIsCurrentTrueAndDayOfYear(i);
            Assert.assertTrue(currentSiteImage.getIsCurrent());

        }

    }

    private Integer getTestDayOfYear(String date) {
        DateTimeFormatter f = DateTimeFormat.forPattern("MM/dd/y");
        DateTime dateTime = f.parseDateTime(date);
        return dateTime.dayOfYear().get();
    }

    @Test
    public void randomSiteImageTest() {
        Collection<SiteImage> siteImages = siteImageRepository.findByBannerImageTrueAndIsActiveTrue();
        int activeBannerCount = siteImages.size();
        for (int i = 0; i < 500; i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, activeBannerCount);
            SiteImage siteImage = (SiteImage) siteImages.toArray()[randomNum];
            assertTrue(siteImage.getIsActive().equals(true));
        }

    }
}