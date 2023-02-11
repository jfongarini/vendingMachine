package com.domain.operation.response;

import com.util.CommonResponse;
import com.domain.operation.data.OperationCancelData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = OperationCancelResponse.Builder.class)
public class OperationCancelResponse extends CommonResponse<OperationCancelData> {

    protected OperationCancelResponse(Builder builder){
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<OperationCancelData,Builder>{

        @Override
        public OperationCancelResponse build() {
            return new OperationCancelResponse(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
