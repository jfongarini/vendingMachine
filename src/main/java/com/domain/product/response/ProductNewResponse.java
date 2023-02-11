package com.domain.product.response;

import com.util.CommonResponse;
import com.domain.product.data.ProductNewData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = ProductNewResponse.Builder.class)
public class ProductNewResponse extends CommonResponse<ProductNewData> {

    protected ProductNewResponse(Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<ProductNewData, Builder> {

        @Override
        public ProductNewResponse build() {
            return new ProductNewResponse(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
