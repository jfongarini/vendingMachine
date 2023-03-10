package com.domain.vendingMachine.data;


import java.io.Serializable;
import java.util.ArrayList;

public class VmInsertCoinData implements Serializable {

    private String name;
    private Double value;
    private Long quantity;

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

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
