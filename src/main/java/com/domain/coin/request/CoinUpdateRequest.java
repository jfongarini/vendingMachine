package com.domain.coin.request;


import java.io.Serializable;
import java.math.BigDecimal;

public class CoinUpdateRequest implements Serializable {

    private String name;
    private BigDecimal value;

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
}
