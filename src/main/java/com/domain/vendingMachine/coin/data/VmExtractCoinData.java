package com.domain.vendingMachine.coin.data;


import java.io.Serializable;
import java.math.BigDecimal;

public class VmExtractCoinData implements Serializable {

    private String name;
    private BigDecimal value;
    private Long quantityToDelete;
    private Long oldQuantity;
    private Long newQuantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
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
