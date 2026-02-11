package com.kristag.pft.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "accounts")
public class Account extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "opening_balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal openingBalance;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();


    protected Account() { /* JPA */ }

    public Account(User user, String name, String currency, BigDecimal openingBalance) {
        this.user = user;
        this.name = name;
        this.currency = currency;
        this.openingBalance = openingBalance;
    }

    public void update(String name, String currency, BigDecimal openingBalance) {
        this.name = name;
        this.currency = currency;
        this.openingBalance = openingBalance;
    }

    public User getUser() { return user; }
    public String getName() { return name; }
    public String getCurrency() { return currency; }
    public BigDecimal getOpeningBalance() { return openingBalance; }
    public Instant getCreatedAt() { return createdAt; }
}