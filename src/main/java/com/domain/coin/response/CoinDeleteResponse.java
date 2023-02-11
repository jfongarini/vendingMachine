package com.domain.coin.response;

import com.domain.coin.data.CoinDeleteData;
import com.util.CommonResponse;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = CoinDeleteResponse.Builder.class)
public class CoinDeleteResponse extends CommonResponse<CoinDeleteData> {

    protected CoinDeleteResponse(CoinDeleteResponse.Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<CoinDeleteData, CoinDeleteResponse.Builder> {

        @Override
        public CoinDeleteResponse build() {
            return new CoinDeleteResponse(this);
        }

        @Override
        protected CoinDeleteResponse.Builder self() {
            return this;
        }
    }
}