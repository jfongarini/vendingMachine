package com.domain.vendingMachine.request;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

public class VendingMachineNewRequest implements Serializable {

    @NotEmpty
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
