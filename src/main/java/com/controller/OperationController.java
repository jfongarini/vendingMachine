package com.controller;

import com.domain.operation.request.OperationAddCoinsRequest;
import com.domain.operation.request.OperationSelectProductRequest;
import com.domain.operation.response.*;
import com.service.OperationService;
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
@Tag(name = "Operations")
public class OperationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationController.class);

    @Autowired
    private OperationService service;

    @PostMapping("operations")
    public ResponseEntity<OperationNewResponse> newOperation(@RequestParam int vendingMachine){
        try {
            OperationNewResponse response = service.newOperation(vendingMachine);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e){
            LOGGER.error("Error in New Operation service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OperationNewResponse.Builder().build());
        }
    }

    @PostMapping("operations/coins")
    public  ResponseEntity<OperationAddCoinsResponse> addCoinsOperation(@RequestHeader(name = "Authorization", defaultValue = "")  String token, @RequestBody @Valid OperationAddCoinsRequest request, BindingResult result){
        try {
            OperationAddCoinsResponse response = service.addCoinsOperation(token,request,result);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e){
            LOGGER.error("Error in Insert Coins in Operation service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OperationAddCoinsResponse.Builder().build());
        }
    }

    @GetMapping("operations/coins")
    public  ResponseEntity<OperationGetCoinsResponse> getCoinsOperation(@RequestHeader(name = "Authorization", defaultValue = "")  String token){
        try {
            OperationGetCoinsResponse response = service.getCoinsOperation(token);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e){
            LOGGER.error("Error in Get Coins from Operation service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OperationGetCoinsResponse.Builder().build());
        }
    }

    @PostMapping("operations/product")
    public  ResponseEntity<OperationSelectProductResponse> addProductOperation(@RequestHeader(name = "Authorization", defaultValue = "")  String token, @RequestBody @Valid OperationSelectProductRequest request, BindingResult result){
        try {
            OperationSelectProductResponse response = service.addProductOperation(token,request,result);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e){
            LOGGER.error("Error in Insert Product in Operation service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OperationSelectProductResponse.Builder().build());
        }
    }

    @GetMapping("operations/product")
    public  ResponseEntity<OperationGetSelectedProductsResponse> getProductOperation(@RequestHeader(name = "Authorization", defaultValue = "")  String token){
        try {
            OperationGetSelectedProductsResponse response = service.getProductOperation(token);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e){
            LOGGER.error("Error in Get Product from Operation service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OperationGetSelectedProductsResponse.Builder().build());
        }
    }

    @PostMapping("operations/accept")
    public  ResponseEntity<OperationAcceptResponse> acceptOperation(@RequestHeader(name = "Authorization", defaultValue = "")  String token){
        try {
            OperationAcceptResponse response = service.acceptOperation(token);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e){
            LOGGER.error("Error in Accept Operation service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OperationAcceptResponse.Builder().build());
        }
    }

    @PostMapping("operations/cancel")
    public  ResponseEntity<OperationCancelResponse> cancelOperation(@RequestHeader(name = "Authorization", defaultValue = "")  String token){
        try {
            OperationCancelResponse response = service.cancelOperation(token);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e){
            LOGGER.error("Error in Cancel Operation service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OperationCancelResponse.Builder().build());
        }
    }
}
