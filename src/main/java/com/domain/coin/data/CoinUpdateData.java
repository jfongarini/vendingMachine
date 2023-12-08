package com.domain.coin.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class CoinUpdateData implements Serializable {
    private int id;
    private String name;
    private BigDecimal value;

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

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
