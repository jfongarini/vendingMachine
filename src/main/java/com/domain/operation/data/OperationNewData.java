package com.domain.operation.data;

import java.io.Serializable;

public class OperationNewData implements Serializable {

    private int operation;
    private String status;
    private String token;

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
