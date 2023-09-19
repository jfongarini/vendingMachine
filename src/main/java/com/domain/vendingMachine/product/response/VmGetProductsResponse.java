package com.domain.vendingMachine.product.response;

import com.util.CommonResponse;

import com.domain.vendingMachine.product.data.VmGetProductsData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = VmGetProductsResponse.Builder.class)
public class VmGetProductsResponse extends CommonResponse<VmGetProductsData> {

    protected VmGetProductsResponse(VmGetProductsResponse.Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<VmGetProductsData, VmGetProductsResponse.Builder> {

        @Override
        public VmGetProductsResponse build() {
            return new VmGetProductsResponse(this);
        }

        @Override
        protected VmGetProductsResponse.Builder self() {
            return this;
        }
    }
}
