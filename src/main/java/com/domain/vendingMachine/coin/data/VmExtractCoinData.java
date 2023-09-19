package com.domain.vendingMachine.coin.data;


import java.io.Serializable;

public class VmExtractCoinData implements Serializable {

    private String name;
    private Double value;
    private Long quantityToDelete;
    private Long oldQuantity;
    private Long newQuantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Long getQuantityToDelete() {
        return quantityToDelete;
    }

    public void setQuantityToDelete(Long quantityToDelete) {
        this.quantityToDelete = quantityToDelete;
    }

    public Long getOldQuantity() {
        return oldQuantity;
    }

    public void setOldQuantity(Long oldQuantity) {
        this.oldQuantity = oldQuantity;
    }

    public Long getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(Long newQuantity) {
        this.newQuantity = newQuantity;
    }
}
