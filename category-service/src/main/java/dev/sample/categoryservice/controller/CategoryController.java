package dev.sample.categoryservice.controller;

import dev.sample.categoryservice.dto.ApiResponse;
import dev.sample.categoryservice.dto.CategoryRequest;
import dev.sample.categoryservice.dto.CategoryResponse;
import dev.sample.categoryservice.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest request) {
        log.info("POST /api/v1/categories - Creating category: {}", request.getName());
        CategoryResponse category = categoryService.createCategory(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Category created successfully", category));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        log.info("GET /api/v1/categories - Fetching all categories");
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(
                ApiResponse.success("Categories fetched successfully", categories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Long id) {
        log.info("GET /api/v1/categories/{} - Fetching category", id);
        CategoryResponse category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Category fetched successfully", category));
    }

    @GetMapping("/by-name")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryByName(@RequestParam String name) {
        log.info("GET /api/v1/categories/by-name?name={} - Fetching category", name);
        CategoryResponse category = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(
                ApiResponse.success("Category fetched successfully", category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        log.info("PUT /api/v1/categories/{} - Updating category", id);
        CategoryResponse updated = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(
                ApiResponse.success("Category updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        log.info("DELETE /api/v1/categories/{} - Deleting category", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(
                ApiResponse.success("Category deleted successfully", null));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getActiveCategories() {
        log.info("GET /api/v1/categories/active - Fetching active categories");
        List<CategoryResponse> categories = categoryService.getActiveCategories();
        return ResponseEntity.ok(
                ApiResponse.success("Active categories fetched successfully", categories));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> searchCategoriesByName(
            @RequestParam String name) {
        log.info("GET /api/v1/categories/search?name={} - Searching categories", name);
        List<CategoryResponse> categories = categoryService.searchCategoriesByName(name);
        return ResponseEntity.ok(
                ApiResponse.success("Search results", categories));
    }
}
