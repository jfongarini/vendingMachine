package com.domain.vendingMachine.operation.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VmGetOperationData implements Serializable {

    private int operationId;
    private int vendingMachine;
    private Double value;
    private Date date;
    private String status;
    List<VmGetOperationCoinData> coins = new ArrayList<>();
    List<VmGetOperationProductData> products = new ArrayList<>();

    public int getOperationId() {
        return operationId;
    }

    public void setOperationId(int operationId) {
        this.operationId = operationId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getVendingMachine() {
        return vendingMachine;
    }

    public void setVendingMachine(int vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    public List<VmGetOperationCoinData> getCoins() {
        return coins;
    }

    public void setCoins(List<VmGetOperationCoinData> coins) {
        this.coins = coins;
    }

    public List<VmGetOperationProductData> getProducts() {
        return products;
    }

    public void setProducts(List<VmGetOperationProductData> products) {
        this.products = products;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
