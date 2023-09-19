package com.domain.vendingMachine.product.data;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VmExtractProductsData implements Serializable {

    private String vmName;
    private List<VmExtractProductData> deletedProducts = new ArrayList<>();

    public String getVmName() {
        return vmName;
    }

    public void setVmName(String vmName) {
        this.vmName = vmName;
    }

    public List<VmExtractProductData> getDeletedProducts() {
        return deletedProducts;
    }

    public void setDeletedProducts(List<VmExtractProductData> deletedProducts) {
        this.deletedProducts = deletedProducts;
    }
}
