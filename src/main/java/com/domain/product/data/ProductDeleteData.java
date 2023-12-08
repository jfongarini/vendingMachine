package com.domain.product.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductDeleteData implements Serializable {

    private String name;
    private BigDecimal price;
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
