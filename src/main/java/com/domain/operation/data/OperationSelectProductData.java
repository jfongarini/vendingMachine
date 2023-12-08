package com.domain.operation.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class OperationSelectProductData implements Serializable {

    private int operation;
    private String name;
    private String code;
    private BigDecimal price;

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
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
