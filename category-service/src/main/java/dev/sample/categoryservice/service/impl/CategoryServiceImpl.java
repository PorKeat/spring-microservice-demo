package dev.sample.categoryservice.service.impl;

import dev.sample.categoryservice.dto.CategoryRequest;
import dev.sample.categoryservice.dto.CategoryResponse;
import dev.sample.categoryservice.exception.DuplicateResourceException;
import dev.sample.categoryservice.exception.ResourceNotFoundException;
import dev.sample.categoryservice.model.Category;
import dev.sample.categoryservice.repository.CategoryRepository;
import dev.sample.categoryservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        log.info("Creating category with name: {}", request.getName());

        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException(
                    "Category with name '" + request.getName() + "' already exists");
        }

        Category category = mapToEntity(request);
        Category saved = categoryRepository.save(category);
        log.info("Category created successfully with id: {}", saved.getId());
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        log.info("Fetching category with id: {}", id);
        return mapToResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryByName(String name) {
        log.info("Fetching category with name: {}", name);
        Category category = categoryRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with name: " + name));
        return mapToResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        log.info("Fetching all categories");
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        log.info("Updating category with id: {}", id);

        Category existing = findOrThrow(id);

        if (!existing.getName().equalsIgnoreCase(request.getName())
                && categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException(
                    "Category with name '" + request.getName() + "' already exists");
        }

        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setActive(request.getActive() == null ? Boolean.TRUE : request.getActive());

        Category updated = categoryRepository.save(existing);
        log.info("Category updated successfully with id: {}", updated.getId());
        return mapToResponse(updated);
    }

    @Override
    public void deleteCategory(Long id) {
        log.info("Deleting category with id: {}", id);
        Category category = findOrThrow(id);
        categoryRepository.delete(category);
        log.info("Category deleted successfully with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getActiveCategories() {
        log.info("Fetching active categories");
        return categoryRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> searchCategoriesByName(String name) {
        log.info("Searching categories by name: {}", name);
        return categoryRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private Category findOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + id));
    }

    private Category mapToEntity(CategoryRequest request) {
        return Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .active(request.getActive() == null ? Boolean.TRUE : request.getActive())
                .build();
    }

    private CategoryResponse mapToResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .active(category.getActive())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
