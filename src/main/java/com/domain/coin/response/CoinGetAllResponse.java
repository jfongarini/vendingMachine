package com.domain.coin.response;

import com.domain.coin.data.CoinGetAllData;
import com.util.CommonResponse;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = CoinGetAllResponse.Builder.class)
public class CoinGetAllResponse extends CommonResponse<CoinGetAllData> {

    protected CoinGetAllResponse(CoinGetAllResponse.Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<CoinGetAllData, CoinGetAllResponse.Builder> {

        @Override
        public CoinGetAllResponse build() {
            return new CoinGetAllResponse(this);
        }

        @Override
        protected CoinGetAllResponse.Builder self() {
            return this;
        }
    }
}