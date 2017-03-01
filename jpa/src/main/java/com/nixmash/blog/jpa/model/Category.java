package com.nixmash.blog.jpa.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "categories")
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "getCategoryCounts",
                query = "select count(*) as `categoryCount`, c.category_value, c.category_id from categories c " +
                        " inner join post_category_ids pc on c.category_id = pc.category_id " +
                        " inner join posts p on pc.post_id = p.post_id " +
                        " where p.is_published = true " +
                        "group by c.category_value order by categoryCount DESC;",
                resultClass = Category.class)
})
public class Category implements Serializable {

    private static final long serialVersionUID = -5531381747015731447L;

    private long categoryId;
    private String categoryValue;
    private Set<Post> posts;
    private int categoryCount = 0;

    public Category() {
    }

    public Category(String categoryValue) {
        this.categoryValue = categoryValue;
    }

    public Category(Long categoryId, String categoryValue) {
        this.categoryId = categoryId;
        this.categoryValue = categoryValue;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "category_id", nullable = false)
    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
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
            joinColumns={@JoinColumn(name="category_id", referencedColumnName="category_id")},
            inverseJoinColumns={@JoinColumn(name="post_id", referencedColumnName="post_id")})
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

    public void update(final String categoryValue) {
        this.categoryValue = categoryValue;
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
