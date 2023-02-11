package com.domain.operation.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OperationGetCoinsData implements Serializable {

    private int operation;
    private String totalValue;
    private List<OperationGetCoinData> coins = new ArrayList<>();

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public List<OperationGetCoinData> getCoins() {
        return coins;
    }

    public void setCoins(List<OperationGetCoinData> coins) {
        this.coins = coins;
    }

    public String getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }
}
