package com.domain.user.data;

import java.io.Serializable;

public class UserData implements Serializable {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
