package com.domain.operation.response;

import com.util.CommonResponse;
import com.domain.operation.data.OperationSelectProductData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = OperationSelectProductResponse.Builder.class)
public class OperationSelectProductResponse extends CommonResponse<OperationSelectProductData> {

    protected OperationSelectProductResponse(Builder builder){ super(builder);}

    public static class Builder extends CommonResponse.Builder<OperationSelectProductData,Builder>{

        @Override
        public OperationSelectProductResponse build() {
            return new OperationSelectProductResponse(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
