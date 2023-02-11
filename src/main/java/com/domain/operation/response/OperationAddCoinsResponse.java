package com.domain.operation.response;

import com.util.CommonResponse;
import com.domain.operation.data.OperationAddCoinsData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = OperationAddCoinsResponse.Builder.class)
public class OperationAddCoinsResponse extends CommonResponse<OperationAddCoinsData> {

    protected OperationAddCoinsResponse(Builder builder){
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<OperationAddCoinsData,Builder>{

        @Override
        public OperationAddCoinsResponse build() {
            return new OperationAddCoinsResponse(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
