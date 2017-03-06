package com.nixmash.blog.jpa.dto;

import com.nixmash.blog.jpa.model.Category;

import java.io.Serializable;

/**
 * Created by daveburke on 6/22/16.
 */
public class CategoryDTO implements Serializable {

    private static final Long serialVersionUID = -4809849404139121173L;

    private Long categoryId = 1L;
    private String categoryValue;
    private int categoryCount = 0;
    private Boolean isActive = true;
    private Boolean isDefault = true;

    public CategoryDTO() {
    }

    public CategoryDTO(String categoryValue) {
        this.categoryValue = categoryValue;
    }


    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryValue() {
        return categoryValue;
    }

    public void setCategoryValue(String categoryValue) {
        this.categoryValue = categoryValue;
    }

    public int getCategoryCount() {
        return categoryCount;
    }

    public void setCategoryCount(int categoryCount) {
        this.categoryCount = categoryCount;
    }

    public CategoryDTO(Long categoryId, String categoryValue, int categoryCount, Boolean isActive, Boolean isDefault) {
        this.categoryId = categoryId;
        this.categoryValue = categoryValue;
        this.categoryCount = categoryCount;
        this.isActive = isActive;
        this.isDefault = isDefault;
    }

    public CategoryDTO(Category category) {
        this.categoryId = category.getCategoryId();
        this.categoryValue = category.getCategoryValue();
        this.categoryCount = category.getPosts().size();
        this.isActive = category.getIsActive();
        this.isDefault = category.getIsDefault();
    }
}

