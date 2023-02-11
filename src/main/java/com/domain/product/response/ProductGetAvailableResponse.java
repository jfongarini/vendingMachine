package com.domain.product.response;

import com.util.CommonResponse;
import com.domain.product.data.ProductGetAvailableData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = ProductGetAvailableResponse.Builder.class)
public class ProductGetAvailableResponse extends CommonResponse<ProductGetAvailableData> {

    protected ProductGetAvailableResponse(Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<ProductGetAvailableData, Builder> {

        @Override
        public ProductGetAvailableResponse build() {
            return new ProductGetAvailableResponse(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}