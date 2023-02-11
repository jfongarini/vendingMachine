package com.domain.product.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductGetAllData implements Serializable {

    private List<ProductGetData> productList = new ArrayList<>();

    public List<ProductGetData> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductGetData> productList) {
        this.productList = productList;
    }
}
