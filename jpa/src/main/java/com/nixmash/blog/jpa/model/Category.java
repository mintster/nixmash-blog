package com.nixmash.blog.jpa.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "categories")
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "getCategoryCounts",
                query = "select count(*) as `categoryCount`, c.category_value, c.category_id, " +
                        "c.is_active, c.is_default from categories c " +
                        "inner join post_category_ids pc on c.category_id = pc.category_id " +
                        "inner join posts p on pc.post_id = p.post_id " +
                        " where p.is_published = true " +
                        "group by c.category_value order by categoryCount DESC;",
                resultClass = Category.class)
})
public class Category implements Serializable {

    private static final Long serialVersionUID = -5531381747015731447L;

    private Long categoryId;
    private String categoryValue;
    private Set<Post> posts;
    private int categoryCount = 0;
    private Boolean isDefault;
    private Boolean isActive;

    public Category() {
    }

    public Category(String categoryValue) {
        this.categoryValue = categoryValue;
    }

    public Category(Long categoryId, String categoryValue, Boolean isActive, Boolean isDefault) {
        this.categoryId = categoryId;
        this.categoryValue = categoryValue;
        this.isActive = isActive;
        this.isDefault = isDefault;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "category_id", nullable = false)
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Column(name = "is_default", nullable = false)
    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Column(name = "is_active", nullable = false)
    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Basic
    @Column(name = "category_value", nullable = false, length = 50)
    public String getCategoryValue() {
        return categoryValue;
    }

    public void setCategoryValue(String categoryValue) {
        this.categoryValue = categoryValue;
    }

    @OneToMany
    @JoinTable(name="post_category_ids",
            joinColumns={@JoinColumn(name="category_id",
                    referencedColumnName="category_id")},
            inverseJoinColumns={@JoinColumn(name="post_id",
                    referencedColumnName="post_id")})
    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts( Set<Post> posts) {
        this.posts = posts;
    }

    @Transient
    public int getCategoryCount() {
        return categoryCount;
    }

    public void setCategoryCount(int categoryCount) {
        this.categoryCount = categoryCount;
    }

    @Override
    public String toString() {
        return getCategoryValue();
    }

    public void update(String categoryValue, Boolean isActive, Boolean isDefault) {
        this.categoryValue = categoryValue;
        this.isActive = isActive;
        this.isDefault = isDefault;
    }

    public void clearDefault() {
        this.isDefault = false;
    }

    public static Builder getBuilder(Long categoryId, String categoryValue) {
        return new Builder(categoryId, categoryValue);
    }

    public static class Builder {

        private Category built;

        public Builder(Long categoryId, String categoryValue) {
            built = new Category();
            built.categoryId = categoryId;
            built.categoryValue = categoryValue;
        }

        public Category build() {
            return built;
        }
    }

}
