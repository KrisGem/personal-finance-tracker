package com.kristag.pft.domain.repository;

import com.kristag.pft.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findAllByUser_Id(UUID userId);
    Optional<Account> findByIdAndUser_Id(UUID id, UUID userId);
}