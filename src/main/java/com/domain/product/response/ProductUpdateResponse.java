package com.domain.product.response;

import com.util.CommonResponse;
import com.domain.product.data.ProductUpdateData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = ProductUpdateResponse.Builder.class)
public class ProductUpdateResponse extends CommonResponse<ProductUpdateData> {

    protected ProductUpdateResponse(Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<ProductUpdateData, Builder> {

        @Override
        public ProductUpdateResponse build() {
            return new ProductUpdateResponse(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}