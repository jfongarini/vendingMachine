package com.domain.vendingMachine.request;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VmInsertProductsRequest implements Serializable {

    @NotEmpty
    private List<VmProductRequest> products = new ArrayList<>();

    public List<VmProductRequest> getProducts() {
        return products;
    }

    public void setProducts(List<VmProductRequest> products) {
        this.products = products;
    }
}
