package com.domain.vendingMachine.product.data;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VmGetProductsData implements Serializable {

    private String vmName;
    private Long quantity;
    private String totalValue;
    private List<VmGetProductData> products = new ArrayList<>();

    public String getVmName() {
        return vmName;
    }

    public void setVmName(String vmName) {
        this.vmName = vmName;
    }

    public List<VmGetProductData> getProducts() {
        return products;
    }

    public void setProducts(List<VmGetProductData> products) {
        this.products = products;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }
}
