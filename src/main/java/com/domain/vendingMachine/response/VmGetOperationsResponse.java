package com.domain.vendingMachine.response;

import com.util.CommonResponse;
import com.domain.vendingMachine.data.VmGetOperationsData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = VmGetOperationsResponse.Builder.class)
public class VmGetOperationsResponse extends CommonResponse<VmGetOperationsData> {

    protected VmGetOperationsResponse(VmGetOperationsResponse.Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<VmGetOperationsData, VmGetOperationsResponse.Builder> {

        @Override
        public VmGetOperationsResponse build() {
            return new VmGetOperationsResponse(this);
        }

        @Override
        protected VmGetOperationsResponse.Builder self() {
            return this;
        }
    }
}
