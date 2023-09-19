package com.controller;

import com.domain.vendingMachine.coin.request.VmInsertCoinsRequest;
import com.domain.vendingMachine.coin.response.VmExtractCoinsResponse;
import com.domain.vendingMachine.coin.response.VmGetCoinsResponse;
import com.domain.vendingMachine.coin.response.VmInsertCoinsResponse;
import com.domain.vendingMachine.operation.response.VmGetOperationResponse;
import com.domain.vendingMachine.operation.response.VmGetOperationsResponse;
import com.domain.vendingMachine.product.request.VmInsertProductsRequest;
import com.domain.vendingMachine.product.response.VmExtractProductsResponse;
import com.domain.vendingMachine.product.response.VmGetProductsResponse;
import com.domain.vendingMachine.product.response.VmInsertProductsResponse;
import com.domain.vendingMachine.request.*;
import com.domain.vendingMachine.response.*;
import com.service.VendingMachineService;
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
@Tag(name = "Vending Machine")
public class VendingMachineController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VendingMachineController.class);

    @Autowired
    private VendingMachineService service;

    @PostMapping("vending-machines/{id}/login")
    public ResponseEntity<VendingMachineLoginResponse> loginVendingMachine(@PathVariable int id) {
        try {
            VendingMachineLoginResponse response = service.loginVendingMachine(id);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Login Vending Machine service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VendingMachineLoginResponse.Builder().build());
        }
    }

    @PostMapping("vending-machines")
    public ResponseEntity<VendingMachineNewResponse> newVendingMachine(@RequestHeader(name = "Authorization", defaultValue = "")  String token, @RequestBody @Valid VendingMachineNewRequest request, BindingResult bindingResult) {
        try {
            VendingMachineNewResponse response = service.newVendingMachine(request, bindingResult);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in New Vending Machine service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VendingMachineNewResponse.Builder().build());
        }
    }

    @DeleteMapping("vending-machines/{id}")
    public ResponseEntity<VendingMachineDeleteResponse> deleteVendingMachine(@RequestHeader(name = "Authorization", defaultValue = "")  String token, @PathVariable int id){
        try {
            VendingMachineDeleteResponse response = service.deleteVendingMachine(id);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Delete Product service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VendingMachineDeleteResponse.Builder().build());
        }
    }

    @GetMapping("vending-machines/{id}")
    public ResponseEntity<VendingMachineGetResponse> getVendingMachine(@RequestHeader(name = "Authorization", defaultValue = "")  String token, @PathVariable int id){
        try {
            VendingMachineGetResponse response = service.getVendingMachine(id);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Get Product service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VendingMachineGetResponse.Builder().build());
        }
    }

    @GetMapping("vending-machines")
    public ResponseEntity<VendingMachineGetAllResponse> getAllVendingMachine(@RequestHeader(name = "Authorization", defaultValue = "")  String token){
        try {
            VendingMachineGetAllResponse response = service.getAllVendingMachine();
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Get All Product service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VendingMachineGetAllResponse.Builder().build());
        }
    }

    @PutMapping("vending-machines")
    public ResponseEntity<VendingMachineUpdateResponse> updateVendingMachine(@RequestHeader(name = "Authorization", defaultValue = "")  String token, @RequestBody @Valid VendingMachineUpdateRequest request){
        try {
            VendingMachineUpdateResponse response = service.updateVendingMachine(request, token);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Update Product service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VendingMachineUpdateResponse.Builder().build());
        }
    }

    //************************
    //vending-machine Coins
    //************************

    @PostMapping("vending-machines/coins")
    public ResponseEntity<VmInsertCoinsResponse> insertCoinsVendingMachine(@RequestHeader(name = "Authorization", defaultValue = "")  String token, @RequestBody @Valid VmInsertCoinsRequest request, BindingResult bindingResult) {
        try {
            VmInsertCoinsResponse response = service.insertCoinsVendingMachine(token, request, bindingResult);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Insert Coins in Vending Machine service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VmInsertCoinsResponse.Builder().build());
        }
    }

    @GetMapping("vending-machines/coins")
    public ResponseEntity<VmGetCoinsResponse> getCoinsVendingMachine(@RequestHeader(name = "Authorization", defaultValue = "")  String token) {
        try {
            VmGetCoinsResponse response = service.getCoinsVendingMachine(token);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Get Coins from Vending Machine service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VmGetCoinsResponse.Builder().build());
        }
    }

    @DeleteMapping("vending-machines/coins")
    public ResponseEntity<VmExtractCoinsResponse> extractCoinsVendingMachine(@RequestHeader(name = "Authorization", defaultValue = "")  String token) {
        try {
            VmExtractCoinsResponse response = service.extractCoinsVendingMachine(token);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Delete Coins of Vending Machine service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VmExtractCoinsResponse.Builder().build());
        }
    }

    //************************
    //vending-machine Products
    //************************


    @PostMapping("vending-machines/products")
    public ResponseEntity<VmInsertProductsResponse> insertProductsVendingMachine(@RequestHeader(name = "Authorization", defaultValue = "")  String token, @RequestBody @Valid VmInsertProductsRequest request, BindingResult bindingResult) {
        try {
            VmInsertProductsResponse response = service.insertProductsVendingMachine(token,request, bindingResult);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Insert Products in Vending Machine service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VmInsertProductsResponse.Builder().build());
        }
    }

    @GetMapping("vending-machines/products")
    public ResponseEntity<VmGetProductsResponse> getProductsVendingMachine(@RequestHeader(name = "Authorization", defaultValue = "")  String token) {
        try {
            VmGetProductsResponse response = service.getProductsVendingMachine(token);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Get Products from Vending Machine service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VmGetProductsResponse.Builder().build());
        }
    }

    @DeleteMapping("vending-machines/products")
    public ResponseEntity<VmExtractProductsResponse> extractProductsVendingMachine(@RequestHeader(name = "Authorization", defaultValue = "")  String token) {
        try {
            VmExtractProductsResponse response = service.extractProductsVendingMachine(token);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Extract Products of Vending Machine service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VmExtractProductsResponse.Builder().build());
        }
    }

    //************************
    //vending-machine Operations
    //************************

    @GetMapping("vending-machines/operations")
    public ResponseEntity<VmGetOperationsResponse> getOperationsVendingMachine(@RequestHeader(name = "Authorization", defaultValue = "")  String token, @RequestParam(required = false) String from, @RequestParam(required = false) String to) {
        try {
            VmGetOperationsResponse response = service.getOperationsVendingMachine(token,from,to);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Get Operations from Vending Machine service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VmGetOperationsResponse.Builder().build());
        }
    }

    @GetMapping("vending-machines/operations/{id}")
    public ResponseEntity<VmGetOperationResponse> getOperationVendingMachine(@RequestHeader(name = "Authorization", defaultValue = "")  String token, @PathVariable int idOperation) {
        try {
            VmGetOperationResponse response = service.getOperationVendingMachine(token,idOperation);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Get Operation from Vending Machine service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VmGetOperationResponse.Builder().build());
        }
    }
}
