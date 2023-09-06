package com.controller;

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

    @PostMapping("vendingmachine/new")
    public ResponseEntity<VendingMachineNewResponse> newVendingMachine(@RequestBody @Valid VendingMachineNewRequest request, BindingResult bindingResult) {
        try {
            VendingMachineNewResponse response = service.newVendingMachine(request, bindingResult);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in New Vending Machine service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VendingMachineNewResponse.Builder().build());
        }
    }

    @DeleteMapping("vendingmachine/delete/{id}")
    public ResponseEntity<VendingMachineDeleteResponse> deleteVendingMachine(@PathVariable int id){
        try {
            VendingMachineDeleteResponse response = service.deleteVendingMachine(id);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Delete Product service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VendingMachineDeleteResponse.Builder().build());
        }
    }

    @GetMapping("vendingmachine/{id}")
    public ResponseEntity<VendingMachineGetResponse> getVendingMachine(@PathVariable int id){
        try {
            VendingMachineGetResponse response = service.getVendingMachine(id);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Get Product service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VendingMachineGetResponse.Builder().build());
        }
    }

    @GetMapping("vendingmachine/all")
    public ResponseEntity<VendingMachineGetAllResponse> getAllVendingMachine(){
        try {
            VendingMachineGetAllResponse response = service.getAllVendingMachine();
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Get All Product service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VendingMachineGetAllResponse.Builder().build());
        }
    }

    @PutMapping("vendingmachine/update/{id}")
    public ResponseEntity<VendingMachineUpdateResponse> updateVendingMachine(@RequestBody @Valid VendingMachineUpdateRequest request, @PathVariable int id){
        try {
            VendingMachineUpdateResponse response = service.updateVendingMachine(request, id);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Update Product service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VendingMachineUpdateResponse.Builder().build());
        }
    }

    //************************
    //vendingmachine Coins
    //************************

    @PostMapping("vendingmachine/{id}/insertCoins")
    public ResponseEntity<VmInsertCoinsResponse> insertCoinsVendingMachine(@PathVariable int id, @RequestBody @Valid VmInsertCoinsRequest request, BindingResult bindingResult) {
        try {
            VmInsertCoinsResponse response = service.insertCoinsVendingMachine(id, request, bindingResult);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Insert Coins in Vending Machine service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VmInsertCoinsResponse.Builder().build());
        }
    }

    @GetMapping("vendingmachine/{id}/getCoins")
    public ResponseEntity<VmGetCoinsResponse> getCoinsVendingMachine(@PathVariable int id) {
        try {
            VmGetCoinsResponse response = service.getCoinsVendingMachine(id);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Get Coins from Vending Machine service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VmGetCoinsResponse.Builder().build());
        }
    }

    @DeleteMapping("vendingmachine/{id}/extractCoins")
    public ResponseEntity<VmExtractCoinsResponse> extractCoinsVendingMachine(@PathVariable int id) {
        try {
            VmExtractCoinsResponse response = service.extractCoinsVendingMachine(id);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Delete Coins of Vending Machine service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VmExtractCoinsResponse.Builder().build());
        }
    }

    @DeleteMapping("vendingmachine/{id}/extractSomeCoins")
    public ResponseEntity<VmExtractCoinsResponse> extractSomeCoinsVendingMachine(@PathVariable int id,@RequestBody @Valid VmExtractSomeCoinsRequest request, BindingResult bindingResult) {
        try {
            VmExtractCoinsResponse response = service.extractSomeCoinsVendingMachine(id,request, bindingResult);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Delete Coins of Vending Machine service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VmExtractCoinsResponse.Builder().build());
        }
    }

    //************************
    //vendingmachine Products
    //************************


    @PostMapping("vendingmachine/{id}/insertProducts")
    public ResponseEntity<VmInsertProductsResponse> insertProductsVendingMachine(@PathVariable int id,@RequestBody @Valid VmInsertProductsRequest request, BindingResult bindingResult) {
        try {
            VmInsertProductsResponse response = service.insertProductsVendingMachine(id,request, bindingResult);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Insert Products in Vending Machine service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VmInsertProductsResponse.Builder().build());
        }
    }

    @GetMapping("vendingmachine/{id}/getProducts")
    public ResponseEntity<VmGetProductsResponse> getProductsVendingMachine(@PathVariable int id) {
        try {
            VmGetProductsResponse response = service.getProductsVendingMachine(id);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Get Products from Vending Machine service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VmGetProductsResponse.Builder().build());
        }
    }

    @DeleteMapping("vendingmachine/{id}/extractProducts")
    public ResponseEntity<VmExtractProductsResponse> extractProductsVendingMachine(@PathVariable int id) {
        try {
            VmExtractProductsResponse response = service.extractProductsVendingMachine(id);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Extract Products of Vending Machine service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VmExtractProductsResponse.Builder().build());
        }
    }

    @DeleteMapping("vendingmachine/{id}/extractSomeProducts")
    public ResponseEntity<VmExtractProductsResponse> extractSomeProductsVendingMachine(@PathVariable int id,@RequestBody @Valid VmExtractSomeProductsRequest request, BindingResult bindingResult) {
        try {
            VmExtractProductsResponse response = service.extractSomeProductsVendingMachine(id,request, bindingResult);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Extract Products of Vending Machine service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VmExtractProductsResponse.Builder().build());
        }
    }

    //************************
    //vendingmachine Operations
    //************************

    @GetMapping("vendingmachine/{id}/getOperations")
    public ResponseEntity<VmGetOperationsResponse> getOperationsVendingMachine(@PathVariable int id, @RequestParam(required = false) String from, @RequestParam(required = false) String to) {
        try {
            VmGetOperationsResponse response = service.getOperationsVendingMachine(id,from,to);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Get Operations from Vending Machine service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VmGetOperationsResponse.Builder().build());
        }
    }

    @GetMapping("vendingmachine/{id}/getOperation")
    public ResponseEntity<VmGetOperationResponse> getOperationVendingMachine(@PathVariable int id, @RequestParam int idOperation) {
        try {
            VmGetOperationResponse response = service.getOperationVendingMachine(id,idOperation);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Get Operation from Vending Machine service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VmGetOperationResponse.Builder().build());
        }
    }
}
