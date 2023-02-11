package com.domain.vendingMachine.data;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VmInsertProductsData implements Serializable {

    private String vmName;
    private List<VmInsertProductData> insertedProducts = new ArrayList<>();

    public String getVmName() {
        return vmName;
    }

    public void setVmName(String vmName) {
        this.vmName = vmName;
    }

    public List<VmInsertProductData> getInsertedProducts() {
        return insertedProducts;
    }

    public void setInsertedProducts(List<VmInsertProductData> insertedProducts) {
        this.insertedProducts = insertedProducts;
    }
}
