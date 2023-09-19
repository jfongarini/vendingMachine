package com.domain.vendingMachine.product.response;

import com.util.CommonResponse;

import com.domain.vendingMachine.product.data.VmExtractProductsData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = VmExtractProductsResponse.Builder.class)
public class VmExtractProductsResponse extends CommonResponse<VmExtractProductsData> {

    protected VmExtractProductsResponse(VmExtractProductsResponse.Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<VmExtractProductsData, VmExtractProductsResponse.Builder> {

        @Override
        public VmExtractProductsResponse build() {
            return new VmExtractProductsResponse(this);
        }

        @Override
        protected VmExtractProductsResponse.Builder self() {
            return this;
        }
    }
}
