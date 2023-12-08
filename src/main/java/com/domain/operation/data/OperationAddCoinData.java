package com.domain.operation.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class OperationAddCoinData implements Serializable {

    private String name;
    private BigDecimal value;
    private Long quantity;

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

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
