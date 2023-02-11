package com.domain.vendingMachine.response;

import com.util.CommonResponse;
import com.domain.vendingMachine.data.VmGetOperationData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = VmGetOperationResponse.Builder.class)
public class VmGetOperationResponse extends CommonResponse<VmGetOperationData> {

    protected VmGetOperationResponse(VmGetOperationResponse.Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<VmGetOperationData, VmGetOperationResponse.Builder> {

        @Override
        public VmGetOperationResponse build() {
            return new VmGetOperationResponse(this);
        }

        @Override
        protected VmGetOperationResponse.Builder self() {
            return this;
        }
    }
}
