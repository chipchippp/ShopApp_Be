package com.example.shopapp_be.controller;


import com.example.shopapp_be.dto.ProductDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @GetMapping
    public ResponseEntity<String> getAllProduct(
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit
    ) {
        return ResponseEntity.ok(String.format("Category, page: " + page + ", limit: " + limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(String.format("Product id: " + id));
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProduct(
            @Valid @ModelAttribute ProductDTO productDTO,
            BindingResult result
    ) {
        try{
            if (result.hasErrors()) {
                List<String> errors = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errors);
            }
            List<MultipartFile> files = productDTO.getFiles();
            files = files == null ? new ArrayList<>() : files;
            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue;
                }
                    if (file.getSize() > 10 * 1024 * 1024) {
                        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                                .body("File size too large!, max size is 10MB");
                    }
                    String contentType = file.getContentType();
                    if (contentType == null || !contentType.startsWith("image/")) {
                        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                                .body("Unsupported media type, only accept image");
                    }
                    String fileName = storeFile(file);
            }
            return ResponseEntity.ok("Product added successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok("Product added " + productDTO);
    }

    private String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//        them UUID vao TRUOC ten file để đảm bảo tên file là duy nhất
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
//        duong dẫn đến thư mục mà bạn muốn lưu file
        Path uploadDir = Paths.get("uploads");
//        ktra va tao muc neu no kh ton tai
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
//        tạo đường dẫn đến file
        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
//        sao chép file vào thư mục
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id) {
        return ResponseEntity.ok("Product updated " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully id: " + id);
    }
}
