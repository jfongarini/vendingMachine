package com.domain.vendingMachine.response;

import com.util.CommonResponse;
import com.domain.vendingMachine.data.VmGetCoinsData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = VmGetCoinsResponse.Builder.class)
public class VmGetCoinsResponse extends CommonResponse<VmGetCoinsData> {

    protected VmGetCoinsResponse(VmGetCoinsResponse.Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<VmGetCoinsData, VmGetCoinsResponse.Builder> {

        @Override
        public VmGetCoinsResponse build() {
            return new VmGetCoinsResponse(this);
        }

        @Override
        protected VmGetCoinsResponse.Builder self() {
            return this;
        }
    }
}
