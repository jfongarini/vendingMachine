package com.domain.operation.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OperationAddCoinsData implements Serializable {

    private int operation;
    private List<OperationAddCoinData> coins = new ArrayList<>();

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public List<OperationAddCoinData> getCoins() {
        return coins;
    }

    public void setCoins(List<OperationAddCoinData> coins) {
        this.coins = coins;
    }
}
