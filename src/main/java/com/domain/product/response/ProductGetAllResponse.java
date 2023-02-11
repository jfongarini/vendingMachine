package com.domain.product.response;

import com.util.CommonResponse;
import com.domain.product.data.ProductGetAllData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = ProductGetAllResponse.Builder.class)
public class ProductGetAllResponse extends CommonResponse<ProductGetAllData> {

    protected ProductGetAllResponse(Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<ProductGetAllData, Builder> {

        @Override
        public ProductGetAllResponse build() {
            return new ProductGetAllResponse(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}