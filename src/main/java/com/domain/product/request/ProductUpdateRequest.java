package com.domain.product.request;


import java.io.Serializable;
import java.math.BigDecimal;

public class ProductUpdateRequest implements Serializable {

    private String code;
    private BigDecimal price;

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
