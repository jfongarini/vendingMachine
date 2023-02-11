package com.domain.operation.response;

import com.util.CommonResponse;
import com.domain.operation.data.OperationGetSelectedProductsData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = OperationGetSelectedProductsResponse.Builder.class)
public class OperationGetSelectedProductsResponse extends CommonResponse<OperationGetSelectedProductsData> {

    protected OperationGetSelectedProductsResponse(Builder builder){
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<OperationGetSelectedProductsData,Builder>{

        @Override
        public OperationGetSelectedProductsResponse build() {
            return new OperationGetSelectedProductsResponse(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
