package com.domain.coin.response;

import com.domain.coin.data.CoinGetData;
import com.util.CommonResponse;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


@JsonDeserialize(builder = CoinGetResponse.Builder.class)
public class CoinGetResponse extends CommonResponse<CoinGetData> {

    protected CoinGetResponse(CoinGetResponse.Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<CoinGetData, CoinGetResponse.Builder> {

        @Override
        public CoinGetResponse build() {
            return new CoinGetResponse(this);
        }

        @Override
        protected CoinGetResponse.Builder self() {
            return this;
        }
    }
}
