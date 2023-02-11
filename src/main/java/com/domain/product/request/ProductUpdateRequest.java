package com.domain.product.request;


import java.io.Serializable;

public class ProductUpdateRequest implements Serializable {

    private String code;
    private Double price;

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
