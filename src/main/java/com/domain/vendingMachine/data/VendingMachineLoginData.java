package com.domain.vendingMachine.data;

import java.io.Serializable;

public class VendingMachineLoginData implements Serializable {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
