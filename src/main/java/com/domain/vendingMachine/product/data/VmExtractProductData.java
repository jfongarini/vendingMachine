package com.domain.vendingMachine.product.data;


import java.io.Serializable;
import java.math.BigDecimal;

public class VmExtractProductData implements Serializable {

    private String name;
    private String code;
    private BigDecimal price;
    private Long quantityToDelete;
    private Long oldQuantity;
    private Long newQuantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getQuantityToDelete() {
        return quantityToDelete;
    }

    public void setQuantityToDelete(Long quantityToDelete) {
        this.quantityToDelete = quantityToDelete;
    }

    public Long getOldQuantity() {
        return oldQuantity;
    }

    public void setOldQuantity(Long oldQuantity) {
        this.oldQuantity = oldQuantity;
    }

    public Long getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(Long newQuantity) {
        this.newQuantity = newQuantity;
    }
}
