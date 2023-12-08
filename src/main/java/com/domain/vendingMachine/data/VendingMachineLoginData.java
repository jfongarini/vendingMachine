package com.domain.vendingMachine.data;

import java.io.Serializable;

public class VendingMachineLoginData implements Serializable {
    private String token;

    private String name;

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
