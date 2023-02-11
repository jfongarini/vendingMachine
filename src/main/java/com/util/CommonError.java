package com.util;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonError implements Serializable {

    private int status;
    private String message;

    public CommonError() {
    }

    public CommonError(Builder builder) {
        this.status = builder.status;
        this.message = builder.message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class Builder {
        private int status;
        private String message;

        public Builder(int status) {
            this.status = status;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public CommonError build() {
            return new CommonError(this);
        }
    }
}
