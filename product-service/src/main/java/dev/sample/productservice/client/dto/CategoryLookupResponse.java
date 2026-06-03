package dev.sample.productservice.client.dto;

import dev.sample.productservice.dto.CategorySummary;
import lombok.Data;

@Data
public class CategoryLookupResponse {

    private boolean success;
    private String message;
    private CategorySummary data;
}
