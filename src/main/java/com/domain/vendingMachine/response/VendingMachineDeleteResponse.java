package com.domain.vendingMachine.response;

import com.util.CommonResponse;
import com.domain.vendingMachine.data.VendingMachineDeleteData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = VendingMachineDeleteResponse.Builder.class)
public class VendingMachineDeleteResponse extends CommonResponse<VendingMachineDeleteData> {

    protected VendingMachineDeleteResponse(Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<VendingMachineDeleteData, Builder> {

        @Override
        public VendingMachineDeleteResponse build() {
            return new VendingMachineDeleteResponse(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}