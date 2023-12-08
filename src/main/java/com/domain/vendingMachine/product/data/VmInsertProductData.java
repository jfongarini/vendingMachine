package com.domain.vendingMachine.product.data;


import java.io.Serializable;
import java.math.BigDecimal;

public class VmInsertProductData implements Serializable {

    private String name;
    private String code;
    private BigDecimal price;
    private Long quantity;

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

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
