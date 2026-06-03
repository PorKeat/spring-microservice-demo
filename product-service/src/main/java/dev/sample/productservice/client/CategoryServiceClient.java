package dev.sample.productservice.client;

import dev.sample.productservice.client.dto.CategoryListResponse;
import dev.sample.productservice.client.dto.CategoryLookupResponse;
import dev.sample.productservice.dto.CategorySummary;
import dev.sample.productservice.exception.InvalidCategoryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceClient {

    private static final String CATEGORY_BY_ID_URL = "http://category-service/api/v1/categories/{id}";
    private static final String CATEGORY_ACTIVE_URL = "http://category-service/api/v1/categories/active";

    private final RestClient.Builder restClientBuilder;

    public CategorySummary getRequiredActiveCategory(Long categoryId) {
        try {
            CategoryLookupResponse response = restClientBuilder.build()
                    .get()
                    .uri(CATEGORY_BY_ID_URL, categoryId)
                    .retrieve()
                    .body(CategoryLookupResponse.class);

            if (response == null || response.getData() == null) {
                throw new InvalidCategoryException("Category id '" + categoryId + "' could not be validated");
            }

            if (Boolean.FALSE.equals(response.getData().getActive())) {
                throw new InvalidCategoryException("Category id '" + categoryId + "' is inactive");
            }
            return response.getData();
        } catch (HttpClientErrorException.NotFound ex) {
            throw new InvalidCategoryException("Category id '" + categoryId + "' does not exist");
        } catch (HttpClientErrorException ex) {
            log.error("Category service returned error while validating category id {}: {}",
                    categoryId, ex.getStatusCode(), ex);
            throw new InvalidCategoryException("Category id '" + categoryId + "' could not be validated");
        } catch (RestClientException ex) {
            log.error("Category service is unavailable while validating category id {}", categoryId, ex);
            throw new InvalidCategoryException(
                    "Category validation is unavailable right now. Please try again later.",
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    public CategorySummary getCategoryById(Long categoryId) {
        try {
            CategoryLookupResponse response = restClientBuilder.build()
                    .get()
                    .uri(CATEGORY_BY_ID_URL, categoryId)
                    .retrieve()
                    .body(CategoryLookupResponse.class);
            return response == null ? null : response.getData();
        } catch (RestClientException ex) {
            log.warn("Unable to fetch category details for id {}", categoryId, ex);
            return null;
        }
    }

    public java.util.List<CategorySummary> getActiveCategories() {
        try {
            CategoryListResponse response = restClientBuilder.build()
                    .get()
                    .uri(CATEGORY_ACTIVE_URL)
                    .retrieve()
                    .body(CategoryListResponse.class);

            if (response == null || response.getData() == null) {
                throw new InvalidCategoryException("Active categories could not be fetched");
            }
            return response.getData();
        } catch (HttpClientErrorException ex) {
            log.error("Category service returned error while fetching active categories: {}", ex.getStatusCode(), ex);
            throw new InvalidCategoryException("Active categories could not be fetched");
        } catch (RestClientException ex) {
            log.error("Category service is unavailable while fetching active categories", ex);
            throw new InvalidCategoryException(
                    "Category service is unavailable right now. Please try again later.",
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
