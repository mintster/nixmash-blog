package com.nixmash.blog.jpa.dto;

import com.nixmash.blog.jpa.model.Category;

import java.io.Serializable;

/**
 * Created by daveburke on 6/22/16.
 */
public class CategoryDTO implements Serializable {

    private static final long serialVersionUID = -4809849404139121173L;

    private long categoryId = -1;
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

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
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

    public CategoryDTO(long categoryId, String categoryValue, Boolean isActive, Boolean isDefault) {
        this.categoryId = categoryId;
        this.categoryValue = categoryValue;
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

