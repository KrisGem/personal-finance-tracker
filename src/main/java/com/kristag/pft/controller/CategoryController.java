package com.kristag.pft.controller;

import com.kristag.pft.domain.entity.Category;
import com.kristag.pft.dto.CategoryCreateRequest;
import com.kristag.pft.dto.CategoryResponse;
import com.kristag.pft.dto.CategoryUpdateRequest;
import com.kristag.pft.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Validated
@Tag(name = "Categories", description = "Manage categories used for transactions")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Create category")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse create(Authentication auth, @Valid @RequestBody CategoryCreateRequest req) {
        UUID userId = (UUID) auth.getPrincipal();
        return categoryService.create(userId, req);
    }

    @Operation(summary = "List categories")
    @GetMapping
    public List<CategoryResponse> list(Authentication auth) {
        UUID userId = (UUID) auth.getPrincipal();
        return categoryService.list(userId);
    }

    @Operation(summary = "Get category by id")
    @GetMapping("/{id}")
    public CategoryResponse get(
            Authentication auth,
            @Parameter(description = "Category id") @PathVariable @NotNull UUID id
    ) {
        UUID userId = (UUID) auth.getPrincipal();
        return categoryService.get(userId, id);
    }

    @Operation(summary = "Update category")
    @PutMapping("/{id}")
    public CategoryResponse update(
            Authentication auth,
            @Parameter(description = "Category id") @PathVariable UUID id,
            @Valid @RequestBody CategoryUpdateRequest req
    ) {
        UUID userId = (UUID) auth.getPrincipal();
        return categoryService.update(userId, id, req);
    }

    @Operation(summary = "Delete category")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            Authentication auth,
            @Parameter(description = "Category id") @PathVariable UUID id
    ) {
        UUID userId = (UUID) auth.getPrincipal();
        categoryService.delete(userId, id);
    }
}