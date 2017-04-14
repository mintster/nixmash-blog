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
import java.util.ArrayList;
import java.util.Collection;

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

    @Transactional
    @Override
    public SiteImage getHomeBanner() {
        int dayOfMonth = DateTime.now().dayOfMonth().get() - 1;

        Collection<SiteImage> siteImages = siteImageRepository.findByBannerImageTrueAndIsActiveTrue();
        int activeBannerCount = siteImages.size();
        int siteImageIndex;

        if (dayOfMonth < activeBannerCount) {
            siteImageIndex = dayOfMonth;
        } else {
            siteImageIndex = dayOfMonth - activeBannerCount;
        }
        SiteImage bannerImage = new ArrayList<>(siteImages).get(siteImageIndex);
        return bannerImage;
    }

//    @Transactional
//    @Override
//    public SiteImage getHomeBanner() {
//        int dayOfMonth = DateTime.now().dayOfMonth().get() - 1;
//
//        Collection<SiteImage> siteImages = siteImageRepository.findByBannerImageTrueAndIsActiveTrue();
//        logger.debug("All Banners Count: " + siteImageRepository.findByBannerImageTrue().size());
//
//        int activeBannerCount = siteImages.size();
//        logger.debug("Active Banners Count: " + activeBannerCount);
//
//        int siteImageIndex;
//
//        logger.debug("Day of Month: " + dayOfMonth);
//        if (dayOfMonth < activeBannerCount) {
//            siteImageIndex = dayOfMonth;
//        } else {
//            siteImageIndex = dayOfMonth - activeBannerCount;
//        }
//        logger.debug("SiteImageIndex: " + siteImageIndex);
//        SiteImage bannerImage = new ArrayList<>(siteImages).get(siteImageIndex);
//        logger.debug("SiteImage ID and filename: " + bannerImage.getSiteImageId() + " | " + bannerImage.getImageFilename());
//        logger.debug("-------------------");
//        return bannerImage;
//    }
//


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



