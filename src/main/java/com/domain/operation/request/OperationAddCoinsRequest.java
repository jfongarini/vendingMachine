package com.domain.operation.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OperationAddCoinsRequest implements Serializable {

    @NotNull
    private int operation;

    @NotEmpty
    private List<OperationAddCoinRequest> coins = new ArrayList<>();

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public List<OperationAddCoinRequest> getCoins() {
        return coins;
    }

    public void setCoins(List<OperationAddCoinRequest> coins) {
        this.coins = coins;
    }
}
