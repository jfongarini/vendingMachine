package com.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = CommonResponse.Builder.class)
public abstract class CommonResponse<DATA extends Serializable> implements Serializable {

    private DATA data;
    private CommonError error;
    private String message;

    protected CommonResponse(Builder<DATA, ?> builder) {
        this.data = builder.data;
        this.error = builder.error;
        this.message = builder.message;
    }

    public DATA getData() {
        return data;
    }

    public void setData(DATA data) {
        this.data = data;
    }

    public CommonError getError() {
        return error;
    }

    public void setError(CommonError error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public abstract static class Builder<DATA extends Serializable, T extends Builder<DATA, T>> {
        private DATA data;
        private CommonError error;
        private String message;

        public T withData(DATA data) {
            this.data = data;
            return self();
        }

        public T withError(CommonError error) {
            this.error = error;
            return self();
        }

        public T withMessage(String message) {
            this.message = message;
            return self();
        }

        public abstract CommonResponse<?> build();

        protected abstract T self();
    }
}
