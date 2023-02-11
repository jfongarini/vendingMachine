package com.domain.operation.data;

import java.io.Serializable;

public class OperationNewData implements Serializable {

    private int operation;
    private String status;

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
}
