package com.domain.vendingMachine.response;

import com.util.CommonResponse;
import com.domain.vendingMachine.data.VendingMachineUpdateData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = VendingMachineUpdateResponse.Builder.class)
public class VendingMachineUpdateResponse extends CommonResponse<VendingMachineUpdateData> {

    protected VendingMachineUpdateResponse(Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<VendingMachineUpdateData, Builder> {

        @Override
        public VendingMachineUpdateResponse build() {
            return new VendingMachineUpdateResponse(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}