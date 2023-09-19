package com.domain.operation.request;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OperationAddCoinsRequest implements Serializable {

    @NotEmpty
    private List<OperationAddCoinRequest> coins = new ArrayList<>();

    public List<OperationAddCoinRequest> getCoins() {
        return coins;
    }

    public void setCoins(List<OperationAddCoinRequest> coins) {
        this.coins = coins;
    }
}
