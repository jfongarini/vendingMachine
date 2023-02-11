package com.domain.vendingMachine.data;


import java.io.Serializable;

public class VmGetOperationProductData implements Serializable {

    private String name;
    private String code;
    private Double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
