package com.domain.product.response;

import com.util.CommonResponse;
import com.domain.product.data.ProductDeleteData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = ProductDeleteResponse.Builder.class)
public class ProductDeleteResponse extends CommonResponse<ProductDeleteData> {

    protected ProductDeleteResponse(Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<ProductDeleteData, Builder> {

        @Override
        public ProductDeleteResponse build() {
            return new ProductDeleteResponse(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}