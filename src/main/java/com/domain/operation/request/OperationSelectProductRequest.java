package com.domain.operation.request;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;


public class OperationSelectProductRequest implements Serializable {

    @NotEmpty
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
