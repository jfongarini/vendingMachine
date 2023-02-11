package com.domain.vendingMachine.response;

import com.util.CommonResponse;
import com.domain.vendingMachine.data.VendingMachineNewData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = VendingMachineNewResponse.Builder.class)
public class VendingMachineNewResponse extends CommonResponse<VendingMachineNewData> {

    protected VendingMachineNewResponse(Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<VendingMachineNewData, Builder> {

        @Override
        public VendingMachineNewResponse build() {
            return new VendingMachineNewResponse(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
