package com.domain.vendingMachine.data;


import java.io.Serializable;

public class VendingMachineNewData implements Serializable {

    private String name;
    private String token;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
