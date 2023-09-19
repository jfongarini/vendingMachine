package com.domain.vendingMachine.coin.response;

import com.util.CommonResponse;
import com.domain.vendingMachine.coin.data.VmInsertCoinsData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = VmInsertCoinsResponse.Builder.class)
public class VmInsertCoinsResponse  extends CommonResponse<VmInsertCoinsData> {

    protected VmInsertCoinsResponse(VmInsertCoinsResponse.Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<VmInsertCoinsData, VmInsertCoinsResponse.Builder> {

        @Override
        public VmInsertCoinsResponse build() {
            return new VmInsertCoinsResponse(this);
        }

        @Override
        protected VmInsertCoinsResponse.Builder self() {
            return this;
        }
    }
}
