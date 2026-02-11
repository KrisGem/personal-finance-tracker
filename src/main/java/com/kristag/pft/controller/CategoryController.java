package com.kristag.pft.controller;

import com.kristag.pft.domain.entity.Category;
import com.kristag.pft.dto.CategoryCreateRequest;
import com.kristag.pft.dto.CategoryResponse;
import com.kristag.pft.dto.CategoryUpdateRequest;
import com.kristag.pft.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse create(Authentication auth, @Valid @RequestBody CategoryCreateRequest req) {
        UUID userId = (UUID) auth.getPrincipal();
        return categoryService.create(userId, req);
    }

    @GetMapping
    public List<CategoryResponse> list(Authentication auth) {
        UUID userId = (UUID) auth.getPrincipal();
        return categoryService.list(userId);
    }

    @GetMapping("/{id}")
    public CategoryResponse get(Authentication auth, @PathVariable UUID id) {
        UUID userId = (UUID) auth.getPrincipal();
        return categoryService.get(userId, id);
    }

    @PutMapping("/{id}")
    public CategoryResponse update(Authentication auth, @PathVariable UUID id, @Valid @RequestBody CategoryUpdateRequest req) {
        UUID userId = (UUID) auth.getPrincipal();
        return categoryService.update(userId, id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Authentication auth, @PathVariable UUID id) {
        UUID userId = (UUID) auth.getPrincipal();
        categoryService.delete(userId, id);
    }
}