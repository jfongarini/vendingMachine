package com.domain.operation.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class OperationAcceptData implements Serializable {

    private int operation;
    private String status;
    private BigDecimal value;
    private String moneyAdded;
    private int numberOfProducts;
    private List<OperationAcceptProductData> products;
    private BigDecimal moneyReturned;
    private List<OperationAcceptCoinData> coinsReturned;

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public int getNumberOfProducts() {
        return numberOfProducts;
    }

    public void setNumberOfProducts(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
    }

    public List<OperationAcceptProductData> getProducts() {
        return products;
    }

    public void setProducts(List<OperationAcceptProductData> products) {
        this.products = products;
    }

    public BigDecimal getMoneyReturned() {
        return moneyReturned;
    }

    public void setMoneyReturned(BigDecimal moneyReturned) {
        this.moneyReturned = moneyReturned;
    }

    public List<OperationAcceptCoinData> getCoinsReturned() {
        return coinsReturned;
    }

    public void setCoinsReturned(List<OperationAcceptCoinData> coinsReturned) {
        this.coinsReturned = coinsReturned;
    }

    public String getMoneyAdded() {
        return moneyAdded;
    }

    public void setMoneyAdded(String moneyAdded) {
        this.moneyAdded = moneyAdded;
    }
}
