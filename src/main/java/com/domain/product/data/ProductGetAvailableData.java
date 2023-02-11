package com.domain.product.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductGetAvailableData implements Serializable {

    private List<String> availableProducts = new ArrayList<>();
    private List<String> usedProducts = new ArrayList<>();

    public List<String> getAvailableProducts() {
        return availableProducts;
    }

    public void setAvailableProducts(List<String> availableProducts) {
        this.availableProducts = availableProducts;
    }

    public List<String> getUsedProducts() {
        return usedProducts;
    }

    public void setUsedProducts(List<String> usedProducts) {
        this.usedProducts = usedProducts;
    }
}
