package com.domain.product.data;

import java.io.Serializable;

public class ProductGetData implements Serializable {
    private int id;
    private String name;
    private String code;
    private Double price;

    public ProductGetData() {
    }

    public ProductGetData(int id, String name, String code, Double price) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
