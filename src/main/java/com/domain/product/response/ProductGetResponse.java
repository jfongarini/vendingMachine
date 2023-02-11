package com.domain.product.response;


import com.util.CommonResponse;
import com.domain.product.data.ProductGetData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


@JsonDeserialize(builder = ProductGetResponse.Builder.class)
public class ProductGetResponse extends CommonResponse<ProductGetData> {

    protected ProductGetResponse(Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<ProductGetData, Builder> {

        @Override
        public ProductGetResponse build() {
            return new ProductGetResponse(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
