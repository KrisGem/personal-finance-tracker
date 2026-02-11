package com.kristag.pft.domain.repository;

import com.kristag.pft.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findAllByUser_Id(UUID userId);
    Optional<Category> findByIdAndUser_Id(UUID id, UUID userId);
}