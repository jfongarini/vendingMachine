package com.domain.vendingMachine.response;


import com.util.CommonResponse;
import com.domain.vendingMachine.data.VendingMachineGetData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


@JsonDeserialize(builder = VendingMachineGetResponse.Builder.class)
public class VendingMachineGetResponse extends CommonResponse<VendingMachineGetData> {

    protected VendingMachineGetResponse(Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<VendingMachineGetData, Builder> {

        @Override
        public VendingMachineGetResponse build() {
            return new VendingMachineGetResponse(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
