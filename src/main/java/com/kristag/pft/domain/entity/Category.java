package com.kristag.pft.domain.entity;

import com.kristag.pft.domain.enums.CategoryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private CategoryType type;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    protected Category() { }

    public Category(User user, String name, CategoryType type) {
        this.user = user;
        this.name = name;
        this.type = type;
    }

    public void update(String name, CategoryType type) {
        this.name = name;
        this.type = type;
    }

    public User getUser() { return user; }
    public String getName() { return name; }
    public CategoryType getType() { return type; }
    public Instant getCreatedAt() { return createdAt; }
}