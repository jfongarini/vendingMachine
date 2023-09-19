package com.domain.vendingMachine.response;

import com.domain.vendingMachine.data.VendingMachineLoginData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.util.CommonResponse;

@JsonDeserialize(builder = VendingMachineLoginResponse.Builder.class)
public class VendingMachineLoginResponse extends CommonResponse<VendingMachineLoginData> {

    protected VendingMachineLoginResponse(VendingMachineLoginResponse.Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<VendingMachineLoginData, VendingMachineLoginResponse.Builder>{

        @Override
        public VendingMachineLoginResponse build() {
            return new VendingMachineLoginResponse(this);
        }

        @Override
        protected VendingMachineLoginResponse.Builder self() {
            return this;
        }
    }
}
