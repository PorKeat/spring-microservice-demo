package dev.sample.categoryservice.service;

import dev.sample.categoryservice.dto.CategoryRequest;
import dev.sample.categoryservice.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse getCategoryById(Long id);

    CategoryResponse getCategoryByName(String name);

    List<CategoryResponse> getAllCategories();

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    void deleteCategory(Long id);

    List<CategoryResponse> getActiveCategories();

    List<CategoryResponse> searchCategoriesByName(String name);
}
