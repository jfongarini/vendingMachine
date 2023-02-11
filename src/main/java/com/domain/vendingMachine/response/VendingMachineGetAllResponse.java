package com.domain.vendingMachine.response;

import com.util.CommonResponse;
import com.domain.vendingMachine.data.VendingMachineGetAllData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = VendingMachineGetAllResponse.Builder.class)
public class VendingMachineGetAllResponse extends CommonResponse<VendingMachineGetAllData> {

    protected VendingMachineGetAllResponse(Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<VendingMachineGetAllData, Builder> {

        @Override
        public VendingMachineGetAllResponse build() {
            return new VendingMachineGetAllResponse(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}