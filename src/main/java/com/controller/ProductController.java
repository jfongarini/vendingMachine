package com.controller;

import com.domain.product.request.ProductNewRequest;
import com.domain.product.request.ProductUpdateRequest;
import com.domain.product.response.*;
import com.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/")
@Tag(name = "Products")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService service;

    @PostMapping("products")
    public ResponseEntity<ProductNewResponse> newProduct(@RequestBody @Valid ProductNewRequest request, BindingResult bindingResult) {
        try {
            ProductNewResponse response = service.newProduct(request, bindingResult);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in New Product service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ProductNewResponse.Builder().build());
        }
    }

    @DeleteMapping("products/{id}")
    public ResponseEntity<ProductDeleteResponse> deleteProduct(@PathVariable int id){
        try {
            ProductDeleteResponse response = service.deleteProduct(id);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Delete Product service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ProductDeleteResponse.Builder().build());
        }
    }

    @GetMapping("products/{id}")
    public ResponseEntity<ProductGetResponse> getProduct(@PathVariable int id){
        try {
            ProductGetResponse response = service.getProduct(id);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Get Product service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ProductGetResponse.Builder().build());
        }
    }

    @GetMapping("products")
    public ResponseEntity<ProductGetAllResponse> getAllProduct(){
        try {
            ProductGetAllResponse response = service.getAllProduct();
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Get All Product service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ProductGetAllResponse.Builder().build());
        }
    }

    @GetMapping("products/available")
    public ResponseEntity<ProductGetAvailableResponse> getAvailableProduct(){
        try {
            ProductGetAvailableResponse response = service.getAvailableProduct();
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Get Available Product service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ProductGetAvailableResponse.Builder().build());
        }
    }

    @PutMapping("products/{id}")
    public ResponseEntity<ProductUpdateResponse> updateProduct(@RequestBody @Valid ProductUpdateRequest request, @PathVariable int id){
        try {
            ProductUpdateResponse response = service.updateProduct(request, id);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Update Product service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ProductUpdateResponse.Builder().build());
        }
    }
}
