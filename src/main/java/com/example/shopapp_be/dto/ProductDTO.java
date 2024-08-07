package com.example.shopapp_be.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 200, message = "Name must be between 5 and 100 characters")
    private String name;
    @Min(value = 0, message = "Price must be greater than 0")
    @Max(value = 1000000000, message = "Price must be less than 1000000000")
    private Double price;
    private String thumbnail;
    private String description;
    @JsonProperty("category_id")
    private String categoryId;
    private MultipartFile files;
}
