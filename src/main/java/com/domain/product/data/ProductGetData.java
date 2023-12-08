package com.domain.product.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductGetData implements Serializable {
    private int id;
    private String name;
    private String code;
    private BigDecimal price;

    public ProductGetData() {
    }

    public ProductGetData(int id, String name, String code, BigDecimal price) {
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
