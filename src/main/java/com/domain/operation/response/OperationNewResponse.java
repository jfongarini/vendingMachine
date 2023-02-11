package com.domain.operation.response;

import com.util.CommonResponse;
import com.domain.operation.data.OperationNewData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = OperationNewResponse.Builder.class)
public class OperationNewResponse extends CommonResponse<OperationNewData> {

    protected OperationNewResponse(Builder builder){
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<OperationNewData,Builder>{

        @Override
        public OperationNewResponse build() {
            return new OperationNewResponse(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
