package dev.sample.productservice.client.dto;

import dev.sample.productservice.dto.CategorySummary;
import lombok.Data;

import java.util.List;

@Data
public class CategoryListResponse {

    private boolean success;
    private String message;
    private List<CategorySummary> data;
}
