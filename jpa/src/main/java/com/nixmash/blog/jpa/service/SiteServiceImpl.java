package com.nixmash.blog.jpa.service;

import com.nixmash.blog.jpa.common.SiteOptions;
import com.nixmash.blog.jpa.dto.SiteOptionDTO;
import com.nixmash.blog.jpa.exceptions.SiteOptionNotFoundException;
import com.nixmash.blog.jpa.model.SiteImage;
import com.nixmash.blog.jpa.model.SiteOption;
import com.nixmash.blog.jpa.repository.SiteImageRepository;
import com.nixmash.blog.jpa.repository.SiteOptionRepository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 */
@Service("siteService")
@Transactional
public class SiteServiceImpl implements SiteService {

    private static final Logger logger = LoggerFactory.getLogger(SiteServiceImpl.class);

    private SiteOptionRepository siteOptionRepository;
    private SiteOptions siteOptions;
    private SiteImageRepository siteImageRepository;

    @Autowired
    public SiteServiceImpl(SiteOptionRepository siteOptionRepository, SiteOptions siteOptions, SiteImageRepository siteImageRepository) {
        this.siteOptionRepository = siteOptionRepository;
        this.siteOptions = siteOptions;
        this.siteImageRepository = siteImageRepository;
    }


    // region SiteImages

    /**
     * <p>Retrieves Home Page Banner based on random selection from Active Banners
     * in table <strong>site_images</strong>.</p>
     *
     * <p>Used when number of active banners is greater than the number of days in the month.</p>
     *
     * @return SiteImage object
     */
    @Transactional
    @Override
    public SiteImage getHomeBanner() {
        int dayOfYear = DateTime.now().dayOfYear().get();

        SiteImage siteImage = siteImageRepository.findByIsCurrentTrueAndDayOfYear(dayOfYear);
        if (siteImage == null) {
            resetCurrentSiteImage(dayOfYear);
            siteImage = getNewCurrentSiteImage();
        }
        return siteImage;
    }

    private SiteImage getNewCurrentSiteImage() {
        Collection<SiteImage> siteImages = siteImageRepository.findByBannerImageTrueAndIsActiveTrue();
        int activeBannerCount = siteImages.size();
        int randomNum = ThreadLocalRandom.current().nextInt(0, activeBannerCount);
        SiteImage siteImage = (SiteImage) siteImages.toArray()[randomNum];
        siteImage.setIsCurrent(true);
        siteImageRepository.save(siteImage);
        return siteImage;
    }

    private void resetCurrentSiteImage(int dayOfYear) {
        Collection<SiteImage> all = siteImageRepository.findAll();
        all.forEach(a -> {
            a.setDayOfYear(dayOfYear);
            a.setIsCurrent(false);
        });
        siteImageRepository.save(all);
    }

    /**
     * Displays Home Page  Banner based on Day of the Month
     * Valid when the number of available banners is less than the days in the month
     *
     * @return SiteImage
     */
/*    @Transactional
    @Override
    public SiteImage getHomeBanner() {
        int dayOfMonth = DateTime.now().dayOfMonth().get() - 1;

        Collection<SiteImage> siteImages = siteImageRepository.findByBannerImageTrueAndIsActiveTrue();
        int activeBannerCount = siteImages.size();
        int siteImageIndex = 1;

        if (dayOfMonth < activeBannerCount) {
            siteImageIndex = dayOfMonth;
        } else {
            siteImageIndex = dayOfMonth - activeBannerCount;
        }
        return new ArrayList<>(siteImages).get(siteImageIndex);
    }*/


    /**
     * Used for viewing specific banners in development and client review
     * Mapped to: /dev/banner?id=siteImageId
     *
     * @param siteImageId site_image_id of SiteImage record
     * @return SiteImage
     */
    @Transactional
    @Override
    public SiteImage getHomeBanner(long siteImageId) {
        return siteImageRepository.findBySiteImageId(siteImageId);
    }

    // endregion


    // region SiteOptions

    @Override
    public SiteOption update(SiteOptionDTO siteOptionDTO) throws SiteOptionNotFoundException {
        logger.debug("Updating siteOption property {} with value: {}",
                siteOptionDTO.getName(), siteOptionDTO.getValue());

        SiteOption found = findOptionByName(siteOptionDTO.getName());
        found.update(siteOptionDTO.getName(), siteOptionDTO.getValue());

        try {
            siteOptions.setSiteOptionProperty(siteOptionDTO.getName(), siteOptionDTO.getValue());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error("Error updating SiteOption Properties " + e.getMessage());
        }
        return found;
    }

    @Transactional(readOnly = true)
    @Override
    public SiteOption findOptionByName(String name) throws SiteOptionNotFoundException {

        logger.debug("Finding siteOption property with name: {}", name);
        SiteOption found = siteOptionRepository.findByNameIgnoreCase(name);

        if (found == null) {
            logger.debug("No siteOption property with name: {}", name);
            throw new SiteOptionNotFoundException("No siteOption with property name: " + name);
        }

        return found;
    }

    // endregion

    // region Utility Methods and Sorts

    public Sort sortBySiteImageIdAsc() {
        return new Sort(Sort.Direction.ASC, "siteImageId");
    }

    // endregion
}



