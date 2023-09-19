package com.domain.vendingMachine.coin.response;

import com.util.CommonResponse;
import com.domain.vendingMachine.coin.data.VmExtractCoinsData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = VmExtractCoinsResponse.Builder.class)
public class VmExtractCoinsResponse extends CommonResponse<VmExtractCoinsData> {

    protected VmExtractCoinsResponse(VmExtractCoinsResponse.Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<VmExtractCoinsData, VmExtractCoinsResponse.Builder> {

        @Override
        public VmExtractCoinsResponse build() {
            return new VmExtractCoinsResponse(this);
        }

        @Override
        protected VmExtractCoinsResponse.Builder self() {
            return this;
        }
    }
}
