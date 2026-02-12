package com.kristag.pft.domain.repository;

import com.kristag.pft.domain.entity.Transaction;
import com.kristag.pft.domain.repository.projection.IdNameTotalProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {
    Optional<Transaction> findByIdAndAccount_User_Id(UUID id, UUID userId);

@Query("""
    select coalesce(sum(case when t.amount > 0 then t.amount else 0 end), 0)
    from Transaction t
    where t.account.user.id = :userId
      and t.occurredAt >= :start
      and t.occurredAt < :end
""")
BigDecimal sumIncomeForPeriod(UUID userId, Instant start, Instant end);

@Query("""
    select coalesce(sum(case when t.amount < 0 then -t.amount else 0 end), 0)
    from Transaction t
    where t.account.user.id = :userId
      and t.occurredAt >= :start
      and t.occurredAt < :end
""")
BigDecimal sumExpenseForPeriod(UUID userId, Instant start, Instant end);

@Query("""
    select a.id as id, a.name as name,
           coalesce(sum(case when t.amount < 0 then -t.amount else 0 end), 0) as total
    from Transaction t
    join t.account a
    where a.user.id = :userId
      and t.occurredAt >= :start
      and t.occurredAt < :end
    group by a.id, a.name
    order by total desc
""")
List<IdNameTotalProjection> expenseTotalsByAccount(UUID userId, Instant start, Instant end);

@Query("""
    select c.id as id, c.name as name,
           coalesce(sum(case when t.amount < 0 then -t.amount else 0 end), 0) as total
    from Transaction t
    join t.account a
    join t.category c
    where a.user.id = :userId
      and t.occurredAt >= :start
      and t.occurredAt < :end
    group by c.id, c.name
    order by total desc
""")
List<IdNameTotalProjection> expenseTotalsByCategory(UUID userId, Instant start, Instant end);

@Query("""
    select coalesce(sum(case when t.amount < 0 then -t.amount else 0 end), 0)
    from Transaction t
    where t.account.user.id = :userId
      and t.category is null
      and t.occurredAt >= :start
      and t.occurredAt < :end
""")
BigDecimal expenseTotalUncategorized(UUID userId, Instant start, Instant end);
}