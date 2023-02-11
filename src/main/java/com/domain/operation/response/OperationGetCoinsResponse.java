package com.domain.operation.response;

import com.util.CommonResponse;
import com.domain.operation.data.OperationGetCoinsData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = OperationGetCoinsResponse.Builder.class)
public class OperationGetCoinsResponse extends CommonResponse<OperationGetCoinsData> {

    protected OperationGetCoinsResponse(Builder builder){
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<OperationGetCoinsData,Builder>{

        @Override
        public OperationGetCoinsResponse build() {
            return new OperationGetCoinsResponse(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
