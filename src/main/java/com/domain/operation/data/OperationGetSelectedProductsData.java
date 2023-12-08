package com.domain.operation.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OperationGetSelectedProductsData implements Serializable {

    private int operation;
    private BigDecimal totalValue;
    private List<OperationGetSelectedProductData> products = new ArrayList<>();

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public List<OperationGetSelectedProductData> getProducts() {
        return products;
    }

    public void setProducts(List<OperationGetSelectedProductData> products) {
        this.products = products;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }
}
