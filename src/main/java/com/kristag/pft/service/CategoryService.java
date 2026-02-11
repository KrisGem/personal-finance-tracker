package com.kristag.pft.service;
import com.kristag.pft.domain.entity.Category;
import com.kristag.pft.domain.entity.User;
import com.kristag.pft.domain.repository.CategoryRepository;
import com.kristag.pft.domain.repository.UserRepository;
import com.kristag.pft.dto.CategoryCreateRequest;
import com.kristag.pft.dto.CategoryResponse;
import com.kristag.pft.dto.CategoryUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public CategoryResponse create(UUID userId, CategoryCreateRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        Category category = new Category(user, req.name(), req.type());
        return toResponse(categoryRepository.save(category));
    }

    public List<CategoryResponse> list(UUID userId) {
        return categoryRepository.findAllByUser_Id(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public CategoryResponse get(UUID userId, UUID categoryId) {
        Category c = categoryRepository.findByIdAndUser_Id(categoryId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return toResponse(c);
    }

    public CategoryResponse update(UUID userId, UUID categoryId, CategoryUpdateRequest req) {
        Category c = categoryRepository.findByIdAndUser_Id(categoryId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        c.update(req.name(), req.type());
        return toResponse(categoryRepository.save(c));
    }

    public void delete(UUID userId, UUID categoryId) {
        Category c = categoryRepository.findByIdAndUser_Id(categoryId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        categoryRepository.delete(c);
    }

    private CategoryResponse toResponse(Category c) {
        return new CategoryResponse(
                c.getId(),
                c.getName(),
                c.getType(),
                c.getCreatedAt()
        );
    }
}