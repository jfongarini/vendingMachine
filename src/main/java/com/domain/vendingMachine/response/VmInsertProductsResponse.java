package com.domain.vendingMachine.response;

import com.util.CommonResponse;
import com.domain.vendingMachine.data.VmInsertProductsData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = VmInsertProductsResponse.Builder.class)
public class VmInsertProductsResponse extends CommonResponse<VmInsertProductsData> {

    protected VmInsertProductsResponse(VmInsertProductsResponse.Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<VmInsertProductsData, VmInsertProductsResponse.Builder> {

        @Override
        public VmInsertProductsResponse build() {
            return new VmInsertProductsResponse(this);
        }

        @Override
        protected VmInsertProductsResponse.Builder self() {
            return this;
        }
    }
}
