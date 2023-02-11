package com.domain.vendingMachine.request;


import java.io.Serializable;

public class VendingMachineUpdateRequest implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
