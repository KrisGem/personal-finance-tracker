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
@Table(name = "transactions")
public class Transaction extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Column(length = 255)
    private String note;

    protected Transaction() { }

    public Transaction(Account account, Category category, BigDecimal amount, Instant occurredAt, String note) {
        this.account = account;
        this.category = category;
        this.amount = amount;
        this.occurredAt = occurredAt;
        this.note = note;
    }

    public void update(Account account, Category category, BigDecimal amount, Instant occurredAt, String note) {
        this.account = account;
        this.category = category;
        this.amount = amount;
        this.occurredAt = occurredAt;
        this.note = note;
    }

    public Account getAccount() { return account; }
    public Category getCategory() { return category; }
    public BigDecimal getAmount() { return amount; }
    public Instant getOccurredAt() { return occurredAt; }
    public String getNote() { return note; }
}