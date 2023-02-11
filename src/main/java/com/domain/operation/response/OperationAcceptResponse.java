package com.domain.operation.response;

import com.util.CommonResponse;
import com.domain.operation.data.OperationAcceptData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = OperationAcceptResponse.Builder.class)
public class OperationAcceptResponse extends CommonResponse<OperationAcceptData> {

    protected OperationAcceptResponse(Builder builder){
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<OperationAcceptData,Builder>{

        @Override
        public OperationAcceptResponse build() {
            return new OperationAcceptResponse(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
