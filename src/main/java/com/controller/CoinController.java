package com.controller;

import com.domain.coin.request.CoinNewRequest;
import com.domain.coin.request.CoinUpdateRequest;
import com.domain.coin.response.*;
import com.service.CoinService;
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
@Tag(name = "Coins")
public class CoinController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VendingMachineController.class);

    @Autowired
    private CoinService service;

    @PostMapping("coins")
    public ResponseEntity<CoinNewResponse> newCoin(@RequestBody @Valid CoinNewRequest request, BindingResult bindingResult) {
        try {
            CoinNewResponse response = service.newCoin(request, bindingResult);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in New Coin service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CoinNewResponse.Builder().build());
        }
    }

    @DeleteMapping("coins/{id}")
    public ResponseEntity<CoinDeleteResponse> deleteCoin(@PathVariable int id){
        try {
            CoinDeleteResponse response = service.deleteCoin(id);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Delete Coin service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CoinDeleteResponse.Builder().build());
        }
    }

    @GetMapping("coins/{id}")
    public ResponseEntity<CoinGetResponse> getCoin(@PathVariable int id){
        try {
            CoinGetResponse response = service.getCoin(id);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Get Coin service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CoinGetResponse.Builder().build());
        }
    }

    @GetMapping("coins")
    public ResponseEntity<CoinGetAllResponse> getAllCoin(){
        try {
            CoinGetAllResponse response = service.getAllCoin();
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Get All Coin service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CoinGetAllResponse.Builder().build());
        }
    }

    @PutMapping("coins/{id}")
    public ResponseEntity<CoinUpdateResponse> updateCoin(@RequestBody @Valid CoinUpdateRequest request,@PathVariable int id){
        try {
            CoinUpdateResponse response = service.updateCoin(request, id);
            return Optional.ofNullable(response.getError()).isPresent() ? ResponseEntity.status(response.getError().getStatus()).body(response)
                    : ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            LOGGER.error("Error in Update Coin service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CoinUpdateResponse.Builder().build());
        }
    }

}
